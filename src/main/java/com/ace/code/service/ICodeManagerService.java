package com.ace.code.service;

import com.ace.code.handler.CodeHandler;

/**
 * @author Diliang.Li
 * @date 2017/5/24.
 */
public interface ICodeManagerService {
    /**
     * 根据表名和列名，获取最大的编码
     * @param table	表名
     * @param column	列名
     * @param handler	编码前缀生成器
     * @return	返回下一个编码=前缀+流水号
     */
    String getCode(String table, String column, CodeHandler handler);

    /**
     * 根据表名和列名，获取最大的编码
     * @param table	表名
     * @param column	列名
     * @return	返回下一个编码
     */
    String getCode(String table, String column);
}
