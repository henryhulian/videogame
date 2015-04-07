package com.agming.live.uc.test.dao;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.agming.live.uc.test.ApplicationInit;
import com.gaming.live.common.transaction.TransactionManager;
import com.gaming.live.uc.dao.UserDao;
import com.gaming.live.uc.entity.User;

@Test(priority=2)
public class UserDaoTest {
	
	@Test
	public void test(){
		
		final UserDao userDao = ApplicationInit.getInstance().getApplicationContext().getUserDao();
		TransactionManager transactionManager = ApplicationInit.getInstance().getApplicationContext().getTransactionManagerDataCenter();
		
		transactionManager.doInTransaction(()->{
				User user = new User();
				user.setUserName("test");
				user.setPassword("111111");
				user.setRoles("role");
				user.setSection("c001-s001");
				userDao.createUser(user);
				return user;
		});
		
		transactionManager.doInTransaction(()->{
				User user = userDao.findUserByUserName("test");
				Assert.assertNotNull(user);
				return user;
		});
		
	}
	
}
