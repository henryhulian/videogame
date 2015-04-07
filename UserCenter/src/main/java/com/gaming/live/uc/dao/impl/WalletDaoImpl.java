package com.gaming.live.uc.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import com.gaming.live.common.transaction.TransactionManager;
import com.gaming.live.uc.dao.WalletDao;
import com.gaming.live.uc.entity.Wallet;

public class WalletDaoImpl implements WalletDao{
	
	private QueryRunner queryRunner;
	
	private TransactionManager transactionManager;
	
	@SuppressWarnings("unused")
	private ResultSetHandler<List<Wallet>> resultSetHandler = (ResultSet resultSet) -> {
			List<Wallet> list = new ArrayList<Wallet>();
			if(resultSet.next()){
				Wallet wallet = new Wallet();
				wallet.setUserId(resultSet.getInt("userId"));
				wallet.setBalance(resultSet.getBigDecimal("balance"));
				list.add(wallet);
			}
			return list;
	};
	
	private ResultSetHandler<List<BigDecimal>> balanceResultSetHandler = (ResultSet resultSet) -> {
			List<BigDecimal> list = new ArrayList<BigDecimal>();
			while(resultSet.next()){
				BigDecimal balance=resultSet.getBigDecimal("balance");
				list.add(balance);
			}
			return list;
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
					balanceResultSetHandler,userId).stream().findFirst().orElse(new BigDecimal("0.00"));
			return balance;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
