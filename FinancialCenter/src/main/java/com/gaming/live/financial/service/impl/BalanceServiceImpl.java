package com.gaming.live.financial.service.impl;

import java.math.BigDecimal;

import com.gaming.live.common.transaction.TransactionManager;
import com.gaming.live.financial.dao.WalletDao;

public class BalanceServiceImpl implements com.gaming.live.financial.service.BalanceService {
	
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
