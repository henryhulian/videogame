package com.gaming.live.uc.dao;

import java.math.BigDecimal;

public interface WalletDao {
	
	void increaseBalance( Integer userId , BigDecimal amount );
	void decreaseBalance( Integer userId , BigDecimal amount );
	BigDecimal findBalanceByUserId( Integer userId );
}
