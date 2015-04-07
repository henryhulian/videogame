package com.gaming.live.financial.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import com.gaming.live.common.transaction.TransactionManager;
import com.gaming.live.financial.dao.WalletDao;
import com.gaming.live.financial.entity.Wallet;

public class WalletDaoImpl implements WalletDao{
	
	private QueryRunner queryRunner;
	
	private TransactionManager transactionManager;
	
	@SuppressWarnings("unused")
	private ResultSetHandler<Wallet> resultSetHandler = new ResultSetHandler<Wallet>() {
		@Override
		public Wallet handle(ResultSet resultSet) throws SQLException {
			
			Wallet wallet = null;
			if(resultSet.next()){
				wallet = new Wallet();
				wallet.setUserId(resultSet.getInt("userId"));
				wallet.setBalance(resultSet.getBigDecimal("balance"));
			}
			return wallet;
		}
	};
	
	private ResultSetHandler<BigDecimal> balanceResultSetHandler = new ResultSetHandler<BigDecimal>() {
		@Override
		public BigDecimal handle(ResultSet resultSet) throws SQLException {
			
			BigDecimal balance = null;
			if(resultSet.next()){
				balance=resultSet.getBigDecimal("balance");
			}
			return balance;
		}
	};

	public WalletDaoImpl(QueryRunner queryRunner,
			TransactionManager transactionManager) {
		this.queryRunner = queryRunner;
		this.transactionManager = transactionManager;
	}

	@Override
	public void increaseBalance(Integer userId , BigDecimal amount) {
		try {
			queryRunner.update(transactionManager.getConnection(),
					"UPDATE Wallet SET balance=balance+? WHERE userId=? ",
					amount ,
					userId );
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void decreaseBalance(Integer userId , BigDecimal amount) {
		try {
			queryRunner.update(transactionManager.getConnection(),
					"UPDATE Wallet SET balance=balance-? WHERE userId=? ",
					amount ,
					userId );
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public BigDecimal findBalanceByUserId(Integer userId) {
		try {
			
			BigDecimal balance = queryRunner.query(transactionManager.getConnection(),
					"SELECT balance FROM Wallet WHERE userId=?",
					balanceResultSetHandler,userId);
			return balance;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
