package com.gaming.live.financial.controller;

import com.gaming.live.financial.boundary.Request;
import com.gaming.live.financial.boundary.Response;
import com.gaming.live.financial.service.BalanceService;


public class BalanceController {

	private BalanceService balanceService;
	
	public BalanceController( BalanceService balanceService ) {
		this.balanceService=balanceService;
	}
	
	public void findBalanceByUserId( Request request,Response response ){
		response.setMessage(balanceService.findBalanceByUserId(request.getSession().getUserId()).toString());
	}
}
