package com.ace.code.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ace.code.GenericTest;
import com.ace.code.handler.CodeHandler;


public class CodeManagerServiceTest extends GenericTest {
	@Autowired
	private ICodeManagerService codeManagerService;

	@Test
//	@Ignore
	public void test() throws Exception {
		// 创建8个线程不停地去重入(随机次数n, 0 <= n <=8)获取锁
		List<Thread> threads = createThreads(8);
		//开始任务
		for (Thread t : threads) {
			t.start();
		}
		// 执行60秒
		Thread.sleep(10 * 1000);
		//停止所有线程
		CodeManagerServiceTest.Task.alive = false;
		// 等待所有线程终止
		for (Thread t : threads) {
			t.join();
		}
	}

	@Override
	public Runnable getTask() {
		return new Task(codeManagerService);
	}

	static class Task implements Runnable {
		private ICodeManagerService codeManagerService;

		private static boolean alive = true;

		public Task(ICodeManagerService codeManagerService) {
			this.codeManagerService = codeManagerService;
		}

		@Override
		public void run() {
			while (alive) {
				String code = codeManagerService.getCode("PCM_CHOICE_LIST_HW", "CHOICE_CODE");
				System.out.println("code=" + code);
				code = codeManagerService.getCode("PCM_CHOICE_LIST_HW", "CONFIG_CODE", new CodeHandler() {

					@Override
					public String getPrefix() {
						return "TEST.CODE.";
					}
				});
				System.out.println("code=" + code);
			}
		}
	};
}
