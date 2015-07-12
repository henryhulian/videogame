package com.gaming.live.uc.service;

import java.math.BigDecimal;

public interface BalanceService {
	BigDecimal findBalanceByUserId( Integer userId );
}
