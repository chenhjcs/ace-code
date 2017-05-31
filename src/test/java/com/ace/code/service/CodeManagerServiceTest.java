package com.ace.code.service;

import com.ace.code.handler.CodeHandler;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ace.code.CodeApp;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CodeApp.class)
@WebAppConfiguration()
@Transactional
@Rollback
public class CodeManagerServiceTest {
	@Autowired
	private ICodeManagerService codeManagerService;

	@Test
	@Ignore
	public void test() {
		String code = codeManagerService.getCode("PCM_CHOICE_LIST_HW", "CHOICE_CODE");
		System.out.println("code=" + code);
		code = codeManagerService.getCode("PCM_CHOICE_LIST_HW", "CHOICE_CODE");
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
