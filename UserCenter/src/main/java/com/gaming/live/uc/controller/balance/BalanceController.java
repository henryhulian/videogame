package com.gaming.live.uc.controller.balance;

import com.gaming.live.uc.boundary.Request;
import com.gaming.live.uc.boundary.Response;
import com.gaming.live.uc.service.BalanceService;

public class BalanceController {

	private BalanceService balanceService;
	
	public BalanceController( BalanceService balanceService ) {
		this.balanceService=balanceService;
	}
	
	public void findBalanceByUserId( Request request,Response response ){
		response.setMessage(balanceService.findBalanceByUserId(request.getSession().getUserId()).toString());
	}
}
