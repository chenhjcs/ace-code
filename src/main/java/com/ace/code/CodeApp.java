package com.ace.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ComponentScan({ "com.ace.code.config",  "com.ace.code" , "com.ace.cache" })
@EnableAspectJAutoProxy
public class CodeApp {
//	private static Logger logger = LoggerFactory.getLogger(CodeApp.class);

	private static ConfigurableApplicationContext ctx;

//	private static ThreadLocal<Map<String, Object>> userInfoThreadLocal = new ThreadLocal<Map<String, Object>>();

	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(CodeApp.class);
		ctx = app.run(args);
	}

	/**
	 * 查看SpringBoot生成的Bean
	 * 
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName) {
		return ctx.getBean(beanName);
	}

	/**
	 * 通过class获取Bean.
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		return ctx.getBean(clazz);
	}

	public static ConfigurableApplicationContext getCtx() {
		return ctx;
	}

	public static void setCtx(ConfigurableApplicationContext ctx) {
		CodeApp.ctx = ctx;
	}

//	public static void setCurrentUser(Map<String, Object> userInfo) {
//		userInfoThreadLocal.set(userInfo);
//	}
//
//	/**
//	 * 获取当前用户信息，与当前请求的线程进行绑定 {username:xx,userid:xx,realname:xx}
//	 * 
//	 * @return
//	 */
//	public static Map<String, Object> getCurrentUser() {
////		if (userInfoThreadLocal.get() == null) {
////			logger.warn("获取用户失败");
////		}
//		return userInfoThreadLocal.get();
//	}
//
//	/**
//	 * 获取当前用户登录名称，与当前请求的线程进行绑定
//	 * 
//	 * @return 用户登录名称
//	 */
//	public static String getCurrentUserName() {
//		String rtn = "";
//		if (userInfoThreadLocal.get() != null && userInfoThreadLocal.get().get("username") != null) {
//			rtn = userInfoThreadLocal.get().get("username").toString();
//		}
//		return rtn;
//	}
//
//	/**
//	 * 获取当前用户姓名，与当前请求的线程进行绑定
//	 *
//	 * @return 用户姓名
//	 */
//	public static String getCurrentRealName() {
//		String rtn = "";
//		if (userInfoThreadLocal.get() != null && userInfoThreadLocal.get().get("realname") != null) {
//			rtn = userInfoThreadLocal.get().get("realname").toString();
//		}
//		return rtn;
//	}
}