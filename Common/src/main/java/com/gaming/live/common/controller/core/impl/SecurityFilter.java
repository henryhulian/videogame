package com.gaming.live.common.controller.core.impl;

import org.apache.commons.lang.StringUtils;

import com.gaming.live.common.boundary.ApplicationRequest;
import com.gaming.live.common.boundary.ApplicationResponse;
import com.gaming.live.common.controller.core.Filter;
import com.gaming.live.common.entity.Session;
import com.gaming.live.common.service.RoleService;
import com.gaming.live.common.service.SessionService;

public class SecurityFilter implements
		Filter<ApplicationRequest, ApplicationResponse> {

	private RoleService roleService;
	private SessionService sessionService;
	
	public SecurityFilter(  RoleService roleService , SessionService sessionService) {
		this.roleService=roleService;
		this.sessionService=sessionService;
	}
	
	@Override
	public boolean doFilter(ApplicationRequest request,
			ApplicationResponse response) {

		// 验证请求权限
		// 如果未设置权限，不做权限验证
		if (roleService.isUserAllowed(request.getEventId())) {
			return true;
		}

		// 验证是否有登录票据
		if (StringUtils.isEmpty(request.getToken())) {
			response.setCode(401);
			response.setMessage("Token not existed!");
			return false;
		}

		// 验证是否有有效会话
		Session session = sessionService.findSessionByToken(request.getToken());
		if( session==null ){
			response.setCode(401);
			response.setMessage("Cannot find session!");
			return false;
		}
		

		// 验证是否有权限访问
		if (roleService.isUserAllowed(request.getEventId(),session.getRoles())) {
			request.setSession(session);
			return true;
		}

		response.setCode(403);
		response.setMessage("Not allowed! eventId:"+request.getEventId()+" roles:"+session.getRoles());
		return false;
	}

}
