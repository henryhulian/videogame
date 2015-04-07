package com.gaming.live.uc.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gaming.live.common.boundary.ApplicationRequest;
import com.gaming.live.common.boundary.ApplicationResponse;
import com.gaming.live.common.constant.Code;
import com.gaming.live.common.controller.core.Dispatcher;
import com.gaming.live.common.controller.core.Filter;
import com.gaming.live.common.service.RoleService;
import com.gaming.live.uc.boundary.Request;
import com.gaming.live.uc.boundary.Response;
import com.gaming.live.uc.controller.authentication.AuthenticationController;
import com.gaming.live.uc.controller.balance.BalanceController;
import com.gaming.live.uc.controller.kaptcha.KaptchaController;
import com.gaming.live.uc.controller.register.RegisterController;

public class ControllerDispatcher implements Dispatcher<Request, Response> {
	
	private static final Logger logger = LoggerFactory.getLogger(ControllerDispatcher.class);
	
	public static final int CREATE_KAPTCHA = 1;
	public static final int REISGER = 2;
	public static final int LOGIN = 3;
	public static final int LOGOUT = 4;
	public static final int FIND_BALANCE = 5;
	
	
	private Filter<ApplicationRequest, ApplicationResponse> filter;
	private RoleService roleService;
	private KaptchaController kaptchaController;
	private RegisterController registerController;
	private AuthenticationController authenticationController;
	private BalanceController balanceController;
	
	public ControllerDispatcher(Filter<ApplicationRequest, ApplicationResponse> filter ,
			 RoleService roleService,
			 KaptchaController kaptchaController,
			 AuthenticationController authenticationController,
			 RegisterController registerController,
			 BalanceController balanceController) {
		this.filter=filter;
		this.roleService=roleService;
		this.kaptchaController=kaptchaController;
		this.authenticationController=authenticationController;
		this.registerController=registerController;
		this.balanceController=balanceController;
		
		//配置事件访问权限
		this.roleService.rolesAllowed(FIND_BALANCE, Arrays.asList("user"));
	}

	@Override
	public Response dispatcher(Request request) {
		
		//过滤器，处理权限检查
		Response response = new Response();
		try {
			if( !filter.doFilter(request, response)){
				return  response;
			}
			
			//请求派发
			switch (request.getEventId()) {
			//获取验证码
			case CREATE_KAPTCHA:
				kaptchaController.createKaptcha(request,response);
				break;
			//注册
			case REISGER:
				registerController.regist(request, response);
				break;
			//登录
			case LOGIN:
				authenticationController.login(request, response);
				break;
			//注销
			case LOGOUT:
				authenticationController.logout(request, response);
				break;
			//查询余额
			case FIND_BALANCE:
				balanceController.findBalanceByUserId(request, response);
				break;			
			default:
				response.setCode(Code.ERROR);
				response.setMessage("Cannot find this event. eventId:"+request.getEventId());
				break;
			}
		} catch (Exception e) {
			response.setCode(Code.ERROR);
			response.setMessage("Server error!");
			logger.error("Server error",e);
		}
		
		return response;
	}

	public Filter<ApplicationRequest, ApplicationResponse> getFilter() {
		return filter;
	}

	public void setFilter(Filter<ApplicationRequest, ApplicationResponse> filter) {
		this.filter = filter;
	}
}
