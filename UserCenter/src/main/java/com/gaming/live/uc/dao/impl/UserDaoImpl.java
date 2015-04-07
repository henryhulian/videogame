package com.gaming.live.uc.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import com.gaming.live.common.transaction.TransactionManager;
import com.gaming.live.uc.dao.UserDao;
import com.gaming.live.uc.entity.User;

public class UserDaoImpl implements UserDao{
	
	private QueryRunner queryRunner;
	
	private TransactionManager transactionManager;
	
	private ResultSetHandler<List<User>> resultSetHandler = (ResultSet resultSet)-> {
			List<User> users = new ArrayList<User>();
			while(resultSet.next()){
				User user = new User();
				user.setId(resultSet.getInt("id"));
				user.setUserName(resultSet.getString("userName"));
				user.setPassword(resultSet.getString("password"));
				user.setRoles(resultSet.getString("roles"));
				user.setCreateTime(resultSet.getDate("createTime"));
				users.add(user);
			}
			return users;
	};
	
	public UserDaoImpl( QueryRunner queryRunner, TransactionManager transactionManager) {
		this.queryRunner=queryRunner;
		this.transactionManager=transactionManager;
	}

	@Override
	public User createUser(User user) {
		try {
			queryRunner.update(transactionManager.getConnection(),
					"INSERT INTO User ( userName , password , roles , section , createTime ) VALUES (?,?,?,?,?)"
					,user.getUserName(),user.getPassword(),user.getRoles(),user.getSection(),user.getCreateTime());

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return user;
	}
	
	
	@Override
	public User findUserByUserName(String userName) {
		try {
			User user = queryRunner.query(transactionManager.getConnection(),
					"SELECT id,userName,password,roles,createTime FROM User WHERE userName=?"
					,resultSetHandler,userName).stream().findFirst().orElse(null);
			return user;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
