package com.ace.code.service.impl;

import com.ace.code.handler.CodeHandler;
import com.ace.code.lock.Lock;
import com.ace.code.lock.redis.RedisReentrantLock;
import com.ace.code.service.ICodeManagerService;
import com.ace.code.service.IMybatisService;
import com.ace.code.service.IRedisUtilService;
import com.ace.code.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 编码管理器 <br>
 *
 * 注意：编码规则中的序列号一定是在最后面
 *
 * @author Diliang.Li 2017年5月24日
 */

@Service
public class CodeManagerServiceImpl implements ICodeManagerService {
    @Autowired
    private IRedisUtilService redisUtilService;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private IMybatisService mybatisService;

    public String getCode(String table, String column, CodeHandler handler) {
        String codePrefixStr = handler.getPrefix(); // 影响流水号的部分
        String key = table + "-" + column + "-" + codePrefixStr;
        String code = validateByDate(key);
        if (code != null) {
            return code;
        }

        String[] items = getCodeRule(table, column); // 获取编码规则
        StringBuilder regExp = new StringBuilder(codePrefixStr);
        Long serialSize = null;
        for (String item : items) {
            String[] entry = item.split("[=|:]");
            if (entry[0].equals("serial")) { // 流水号
                regExp.append("([0-9]{").append(entry[1]).append("})");
                serialSize = Long.valueOf(entry[1]);
                break;
            }
        }
        regExp.append("$");

        Jedis jedis = jedisPool.getResource();
        Lock lock = new RedisReentrantLock(jedis, key);
        if (lock.tryLock()) {
            try {
                return getCodeFromRule(table, column, key, regExp.toString(), codePrefixStr, serialSize);
            } finally {
                lock.unlock();
                if(jedis != null) jedis.close();
            }
        } else {
            throw new RuntimeException("服务器繁忙，请稍后重试！");
        }
    }

    public String getCode(String table, String column) {
        String code = validateByDate(table + "-" + column);
        if (code != null)
            return code;

        String[] items = getCodeRule(table, column); // 获取编码规则
        StringBuilder regExp = new StringBuilder("^");
        StringBuilder codePrefix = new StringBuilder();
        Long serialSize = null;
        for(String item : items) {
            String[] entry = item.split("[=|:]");
            if(entry[0].equals("fixed")) { // 固定标识
                regExp.append(entry[1]);
                codePrefix.append(entry[1]);
            } else if(entry[0].equals("date")) { // 日期
                String date = DateUtils.formatDate(new Date(), entry[1]);
                regExp.append(date);
                codePrefix.append(date);
            } else if(entry[0].equals("serial")) { // 流水号
                regExp.append("([0-9]{").append(entry[1]).append("})");
                serialSize = Long.valueOf(entry[1]);
            }
        }
        regExp.append("$");

        String key  = table + "-" + column;

        // redis 分布式锁
        Jedis jedis = jedisPool.getResource();
        Lock lock = new RedisReentrantLock(jedis, key);
        if (lock.tryLock()) {
            try {
                return getCodeFromRule(table, column, key, regExp.toString(), codePrefix.toString(), serialSize);
            } finally {
                lock.unlock();
                if(jedis != null) jedis.close();
            }
        } else {
            throw new RuntimeException("服务器繁忙，请稍后重试！");
        }

    }

    private String getCodeFromRule(String table, String column, String key,
                                   String regExp, String codePrefix, Long serialSize) {
        String queryCode = "SELECT MAX(" + column + ") AS CODE FROM " + table
                + "  WHERE REGEXP_LIKE(" + column + " , '" + regExp + "')";
        Map<String, Object> codeMap = mybatisService.selectOne(queryCode);

        String serialStr = "0";
        if (codeMap != null && !StringUtils.isEmpty(codeMap.get("CODE"))) {
            String maxCode = codeMap.get("CODE").toString(); // "CPC201703270003";
            Pattern pattern = Pattern.compile(regExp);
            Matcher matcher = pattern.matcher(maxCode);
            if (matcher.find()) {
                serialStr = String.valueOf(Integer.parseInt(matcher
                        .group(matcher.groupCount())));
            }
        }

        int expiredTime = 60 * 60 * 24; // 一天
        String codeRuleStr = codePrefix + "=" + DateUtils.formatDate(new Date())
                + "=" + serialSize;
        redisUtilService.set(getCodeKey(key), codeRuleStr, expiredTime);
        redisUtilService.set(key, serialStr, expiredTime);

        return getCodeFromRedis(key, codePrefix, String.valueOf(serialSize));
    }

    private String[] getCodeRule(String table, String column) {
        String queryRule = "SELECT RULE FROM PCM_SYS_CODE_RULE WHERE TABLE_NAME='"
                + table + "' AND COLUMN_NAME='" + column + "'";
        Map<String, Object> ruleMap = mybatisService.selectOne(queryRule);
        if (ruleMap == null	|| StringUtils.isEmpty(ruleMap.get("RULE"))) {
            throw new RuntimeException("[table=" + table + "][column=" + column
                    + "]" + "找不到对应的编码规则！");
        }
        String rule = ruleMap.get("RULE").toString(); // "[fixed=CPC;date=yyyyMMdd;serial=4]";
        rule = rule.substring(1, rule.length() - 1);
        return rule.split("[,|;]");
    }

    /**
     * 根据key，构造存储code=date=serialSize的codeKey
     *
     * @param key
     *            编码的key
     * @return 编码中固定部分编码的key
     */
    private String getCodeKey(String key) {
        return key + "-CODERULE";
    }

    private String validateByDate(String key) {
        String codeRule = redisUtilService.get(getCodeKey(key));
        if (codeRule == null) {
            return null;
        }
        String[] code = codeRule.split("=");
        if (code.length < 3 || !code[1].equals(DateUtils.getDate())) {
            return null;
        } else {
            return getCodeFromRedis(key, code[0], code[2]);
        }
    }

    private String getCodeFromRedis(String key, String code, String serial) {
        Long curSerial = redisUtilService.incrBy(key, 1L); // 流水号递增
        StringBuilder nextCode = new StringBuilder(code);
        int serialLength = Integer.parseInt(serial);
        int curSerialLength = String.valueOf(curSerial).length();
        for (int i = 0; i < (serialLength - curSerialLength); i++) {
            nextCode.append("0");
        }
        nextCode.append(curSerial);
        return nextCode.toString();
    }

}
