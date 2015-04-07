package com.agming.live.test.dao;

import java.math.BigDecimal;








import org.testng.Assert;
import org.testng.annotations.Test;

import com.agming.live.test.ApplicationInit;
import com.gaming.live.common.transaction.Runable;
import com.gaming.live.common.transaction.TransactionManager;
import com.gaming.live.financial.dao.WalletDao;

public class WalletDaoTest {
	
	private WalletDao walletDao = ApplicationInit.getInstance().getApplicationContext().getWalletDao();
	private TransactionManager transactionManager = ApplicationInit.getInstance().getApplicationContext().getTransactionManager();

	@Test(threadPoolSize=100,invocationCount=10,timeOut=3000)
	public void test(){
		
		transactionManager.doInTransaction(new Runable<Void>() {
			@Override
			public Void run() {
				walletDao.increaseBalance(100001, new BigDecimal(1));
				return null;
			}
		});
		
		transactionManager.doInTransaction(new Runable<Void>() {
			@Override
			public Void run() {
				walletDao.decreaseBalance(100001, new BigDecimal(1));
				return null;
			}
		});
		
		transactionManager.doInTransaction(new Runable<Void>() {
			@Override
			public Void run() {
				BigDecimal balance = walletDao.findBalanceByUserId(100001);
				System.out.println("Current balance:"+balance);
				return null;
			}
		});
	}
	
	@Test(dependsOnMethods="test")
	public void test2(){
		
		transactionManager.doInTransaction(new Runable<Void>() {

			@Override
			public Void run() {
				
				BigDecimal balance = walletDao.findBalanceByUserId(100001);
				Assert.assertEquals(new BigDecimal("0.00"), balance);
				
				return null;
			}
			
		});
		
	}
	
}
