package com.gaming.live.uc.usecase.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gaming.live.common.transaction.TransactionManager;
import com.gaming.live.common.utils.DigestUtil;
import com.gaming.live.uc.boundary.Response;
import com.gaming.live.uc.dao.UserDao;
import com.gaming.live.uc.entity.User;
import com.gaming.live.uc.usecase.RegisterService;

public class RegisterServiceImpl implements RegisterService{
	
	private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);
	
	private TransactionManager transactionManager;
	
	private UserDao userDao;

	public RegisterServiceImpl(TransactionManager transactionManager,UserDao userDao) {
		this.userDao = userDao;
		this.transactionManager=transactionManager;
	}

	@Override
	public User register( User user , Response response ) {
		
		return transactionManager.doInTransaction(()->{
				//检查用户是否存在
				if(isUserExisted(user.getUserName())){
					response.setCode(201);
					response.setMessage("User:"+user.getUserName()+" existed!");
					return null;
				}
				
				//加密密码
				user.setPassword(DigestUtil.sha256_base64(user.getPassword()));
				
				//分配权限
				user.setRoles("user");
				
				//分配分区
				user.setSection("c001-s001");
				
				//创建会员
				User userCreated = userDao.createUser(user);
				
				logger.trace("User:"+user+" create success!");
				
				return userCreated;
		});
		
	}

	private boolean isUserExisted(String userName) {
		User user = userDao.findUserByUserName(userName);
		if( user!=null ){
			return true;
		}
		return false;
	}

}
