package com.ace.code.util;

import sun.management.VMManagement;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class PlatformUtils {
	private static Logger logger = LoggerFactory.getLogger(PlatformUtils.class);


	/**
	 * 获取localhost MAC地址
	 * @return 
	 * @throws Exception
	 */
	public static final String MACAddress() {
		try {
			NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			byte[] macBytes = networkInterface.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < macBytes.length; i++) {
				sb.append(String.format("%02X%s", macBytes[i], i < macBytes.length - 1 ? "-" : ""));
			}
			return sb.toString();
		} catch (Exception ex) {
			logger.warn(" {\"result\": \"error in MACAddress method 获取MAC地址出错！\", \"msg\": \"" + ex.getMessage() + "\"}", ex);
			return "";
		}
	}

	/**
	 * 获取当前JVM 的进程ID
	 * @return
	 */
	public static final int JVMPid() {
		try {
			RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
			Field jvm = runtime.getClass().getDeclaredField("jvm");
			jvm.setAccessible(true);
			VMManagement mgmt = (VMManagement) jvm.get(runtime);
			Method pidMethod = mgmt.getClass().getDeclaredMethod("getProcessId");
			pidMethod.setAccessible(true);
			int pid = (Integer) pidMethod.invoke(mgmt);
			return pid;
		} catch (Exception e) {
			logger.warn(" {\"result\": \"error in JVMPid method 获取当前JVM的进程ID出错！\", \"msg\": \"" + e.getMessage() + "\"}");
			return -1;
		}
	}
	
	

	public static void main(String[] args) throws Exception {
		System.out.println(MACAddress());
	}
}
