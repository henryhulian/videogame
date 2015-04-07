package com.gaming.live.uc.usecase;

import java.math.BigDecimal;

public interface BalanceService {
	BigDecimal findBalanceByUserId( Integer userId );
}
