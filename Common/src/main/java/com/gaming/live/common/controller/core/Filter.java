package com.gaming.live.common.controller.core;

import com.gaming.live.common.boundary.ApplicationRequest;
import com.gaming.live.common.boundary.ApplicationResponse;

public interface Filter<RQ extends ApplicationRequest,RP extends ApplicationResponse> {

	/**
	 * 
	 * @param request 请求
	 * @param response 回复
	 * @return 如果通过了过滤器返回true，否则返回false
	 */
	boolean doFilter( RQ request , RP response );

}
