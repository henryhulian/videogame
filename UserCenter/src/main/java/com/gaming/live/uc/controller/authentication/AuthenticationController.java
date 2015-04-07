package com.gaming.live.uc.controller.authentication;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gaming.live.common.entity.Session;
import com.gaming.live.common.service.SessionService;
import com.gaming.live.common.utils.TokenUtil;
import com.gaming.live.uc.boundary.Request;
import com.gaming.live.uc.boundary.Response;
import com.gaming.live.uc.entity.User;
import com.gaming.live.uc.usecase.LoginService;

public class AuthenticationController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
	private SessionService sessionService;
	private LoginService loginService;
	
	public AuthenticationController( 
			SessionService sessionService ,
			LoginService loginService) {
		this.sessionService=sessionService;
		this.loginService=loginService;
	}

	public void login( Request request , Response response ){
		
		User user = loginService.login(request.getUser().getUserName(), request.getUser().getPassword(), response);
		
		if( user!=null ){
			String token = TokenUtil.createRandomToken();
			Session session = new Session();
			session.setToken(token);
			session.setUserId(user.getId());
			session.setUserName(user.getUserName());
			session.setRoles(Arrays.asList(user.getRoles().split(",")));
			sessionService.createSession(session);
			
			logger.info("Session created:"+session);
			
			response.setToken(token);
		}
		
	
		
	}
	
	public void logout( Request request , Response response ){
		response.setToken("logout");
	}
}
