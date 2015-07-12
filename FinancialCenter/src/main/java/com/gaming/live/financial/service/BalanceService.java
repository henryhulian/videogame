package com.gaming.live.financial.service;

import java.math.BigDecimal;

public interface BalanceService {
	BigDecimal findBalanceByUserId( Integer userId );
}
