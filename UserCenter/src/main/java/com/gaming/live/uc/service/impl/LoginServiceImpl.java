package com.gaming.live.uc.service.impl;

import com.gaming.live.common.constant.Code;
import com.gaming.live.common.transaction.TransactionManager;
import com.gaming.live.common.utils.DigestUtil;
import com.gaming.live.uc.boundary.Response;
import com.gaming.live.uc.dao.UserDao;
import com.gaming.live.uc.entity.User;
import com.gaming.live.uc.service.LoginService;

public class LoginServiceImpl implements LoginService{
	
	private UserDao userDao;
	private TransactionManager transactionManager;
	
	public LoginServiceImpl( 
			TransactionManager transactionManager,
			UserDao userDao) {
		this.transactionManager=transactionManager;
		this.userDao=userDao;
	}

	@Override
	public User login(  String userName,  String password ,  Response response ) {
		
		return transactionManager.doInTransaction( ()->{
				User user = userDao.findUserByUserName(userName);
				if( user==null ){
					response.setCode(Code.ERROR);
					response.setMessage("User not existed!");
					return null;
				}
				
				if(!user.getPassword().equals(DigestUtil.sha256_base64(password))){
					response.setCode(Code.ERROR);
					response.setMessage("Password error!");
					return null;
				}
				
				return user;
		});
		
	}

}
