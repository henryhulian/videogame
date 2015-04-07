package com.gaming.live.uc.usecase.impl;

import java.math.BigDecimal;

import com.gaming.live.common.transaction.TransactionManager;
import com.gaming.live.uc.dao.WalletDao;
import com.gaming.live.uc.usecase.BalanceService;

public class BalanceServiceImpl implements BalanceService {
	
	private TransactionManager transactionManager;
	private WalletDao walletDao;
	
	public BalanceServiceImpl(TransactionManager transactionManager,WalletDao walletDao) {
		this.transactionManager=transactionManager;
		this.walletDao=walletDao;
	}

	@Override
	public BigDecimal findBalanceByUserId(Integer userId) {
		return transactionManager.doInTransaction(()->{
			return walletDao.findBalanceByUserId(userId);
		});
	}

}
