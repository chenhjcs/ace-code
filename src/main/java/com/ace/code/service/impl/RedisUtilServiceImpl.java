package com.ace.code.service.impl;

import com.ace.cache.service.IRedisService;
import com.ace.code.service.IRedisUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author Diliang.Li
 * @date 2017/5/24.
 */
@Service
public class RedisUtilServiceImpl implements IRedisUtilService {

    @Autowired
    private IRedisService redisService;

    @Autowired
    private Environment env;

    private String sysName;

    public RedisUtilServiceImpl() {

    }

    public IRedisService getRedisService() {
        return redisService;
    }
    public void setRedisService(IRedisService redisService) {
        this.redisService = redisService;
    }

    private String addSysName(String key) {
        if(this.sysName == null) {
            this.sysName = this.env.getProperty("redis.sysName", "code");
        }
        return this.sysName + ":" + key;
    }

    public void set(String key, String value) {
        getRedisService().set(addSysName(key), value);
    }
    public void set(String key, String value, int timeout) {
        getRedisService().set(addSysName(key), value, timeout);
    }
    public String get(String key) {
        return getRedisService().get(addSysName(key));
    }
    public Long incrBy(String key, Long delta) {
        return getRedisService().incrBy(addSysName(key), delta);
    }
}
