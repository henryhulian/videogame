package com.gaming.live.common.dao.impl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;

import com.gaming.live.common.dao.SessionDao;
import com.gaming.live.common.entity.Session;
import com.gaming.live.common.transaction.TransactionManager;

public class SessionDaoImpl implements SessionDao{

	private TransactionManager transactionManager;
	private QueryRunner queryRunner;
	
	public SessionDaoImpl(TransactionManager transactionManager,
			QueryRunner queryRunner) {
		this.transactionManager = transactionManager;
		this.queryRunner = queryRunner;
	}

	@Override
	public void createSession(Session session) {
		
		try {
			queryRunner.update(transactionManager.getConnection(),
					"INSERT INTO Session ( token , tokenKey , status , roles , userId , userName ,  lastAccessTime , createTime ) VALUES (?,?,?,?,?,?,?,?)"
					,session.getToken(),session.getTokenKey(),session.getStatus(),session.getRolesString(),
					session.getUserId(),session.getUserName(),session.getLastAccessTime(),session.getCreateTime());

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
}
