package com.agming.live.uc.test.service;


import org.testng.Assert;
import org.testng.annotations.Test;

import com.agming.live.uc.test.ApplicationInit;
import com.gaming.live.uc.boundary.Response;
import com.gaming.live.uc.entity.User;
import com.gaming.live.uc.service.RegisterService;

public class RegisterServiceTest {

	@Test
	public void test() {

		RegisterService registerService = ApplicationInit.getInstance().getApplicationContext().getRegisterService();

		User user = new User();
		user.setUserName("testregist");
		user.setPassword("111111");
		Response response = new Response();
		registerService.register(user, response);
		Assert.assertEquals(200, response.getCode());
		
		registerService.register(user, response);
		Assert.assertEquals(201, response.getCode());
		
		
	}

}
