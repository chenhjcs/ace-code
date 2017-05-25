package com.ace.code.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

public interface IRedisUtilService {

	void set(String key, String value);
	void set(String key, String value, int timeout);
	String get(String key);
	Long incrBy(String key, Long delta);
}
