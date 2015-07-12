package com.gaming.live.financial.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gaming.live.common.boundary.ApplicationRequest;
import com.gaming.live.common.boundary.ApplicationResponse;
import com.gaming.live.common.constant.Code;
import com.gaming.live.common.controller.core.Dispatcher;
import com.gaming.live.common.controller.core.Filter;
import com.gaming.live.common.service.RoleService;
import com.gaming.live.financial.boundary.Request;
import com.gaming.live.financial.boundary.Response;

public class ControllerDispatcher implements Dispatcher<Request, Response> {

	private static final Logger logger = LoggerFactory
			.getLogger(ControllerDispatcher.class);

	public static final int FIND_BALANCE = 5;

	private Filter<ApplicationRequest, ApplicationResponse> filter;
	private RoleService roleService;
	private BalanceController balanceController;

	public ControllerDispatcher(
			Filter<ApplicationRequest, ApplicationResponse> filter,
			RoleService roleService, BalanceController balanceController) {
		this.filter = filter;
		this.roleService = roleService;
		this.balanceController = balanceController;

		// 配置事件访问权限
		this.roleService.rolesAllowed(FIND_BALANCE, Arrays.asList("user"));

	}

	@Override
	public Response dispatcher(Request request) {
		// 过滤器，处理权限检查
		Response response = new Response();
		try {
			if (!filter.doFilter(request, response)) {
				return response;
			}
			// 请求派发
			switch (request.getEventId()) {
			// 查询余额
			case FIND_BALANCE:
				balanceController.findBalanceByUserId(request, response);
				break;
			default:
				response.setCode(Code.ERROR);
				response.setMessage("Cannot find this event. eventId:"
						+ request.getEventId());
				break;
			}
		} catch (Exception e) {
			response.setCode(Code.ERROR);
			response.setMessage("Server error!");
			logger.error("Server error", e);
		}

		return response;
	}
}
