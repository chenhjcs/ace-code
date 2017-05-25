package com.ace.code.service;

import java.util.List;
import java.util.Map;

/**
 * 执行SQL工具 <br>
 * @author Diliang.Li
 * @since 2017-05-24
 */
public interface IMybatisService {

	Map<String, Object> selectOne(String sql) ;

	List<Map<String, Object>> selectList(String sql) ;
}
