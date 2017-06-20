package com.ace.code.util;

import sun.management.VMManagement;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class PlatformUtils {
	private static Logger logger = LoggerFactory.getLogger(PlatformUtils.class);


	/**
	 * 获取本机正在使用的网卡的MAC地址（如果有多个启用的网卡时只取第一个）
	 * @return 
	 * @throws Exception
	 */
	public static final String MACAddress() {
		try {
			return getMacAddress();
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
			VMManagement vmManagement = (VMManagement) jvm.get(runtime);
			Method pidMethod = vmManagement.getClass().getDeclaredMethod("getProcessId");
			pidMethod.setAccessible(true);
			int pid = (Integer) pidMethod.invoke(vmManagement);
			return pid;
		} catch (Exception e) {
			logger.warn(" {\"result\": \"error in JVMPid method 获取当前JVM的进程ID出错！\", \"msg\": \"" + e.getMessage() + "\"}");
			return -1;
		}
	}

	/**
	 * 按照"XX-XX-XX-XX-XX-XX"格式，获取本机MAC地址
	 * @return
	 * @throws Exception
	 */
	private static String getMacAddress() throws Exception{
		Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
		while(ni.hasMoreElements()){
			NetworkInterface netI = ni.nextElement();
			if( netI != null && netI.isUp()){
				byte[] bytes = netI.getHardwareAddress();
				if(bytes == null || bytes.length != 6){
					continue;
				}
				StringBuffer sb = new StringBuffer();
				for(byte b:bytes){
					//与11110000作按位与运算以便读取当前字节高4位
					sb.append(Integer.toHexString((b&240)>>4));
					//与00001111作按位与运算以便读取当前字节低4位
					sb.append(Integer.toHexString(b&15));
					sb.append("-");
				}
				sb.deleteCharAt(sb.length()-1);
				return sb.toString().toUpperCase();
			}
		}
		return null;
	}


	public static void main(String[] args) throws Exception {
		System.out.println(MACAddress());
	}
}
