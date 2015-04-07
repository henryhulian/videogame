package com.gaming.live.common.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TransactionManagerImpl implements TransactionManager{
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionManagerImpl.class);

	private ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

	private  DataSource dataSource;
	
	public TransactionManagerImpl( DataSource dataSource ) {
		this.dataSource = dataSource;
	}

	@Override
	public  Connection getConnection() {
		Connection connection = threadLocal.get();
		return connection;
	}
	
	@Override
	public void start() {
		try {
			Connection connection=getConnection();
			if (connection == null) {
				try {
					connection = getDataSource().getConnection();
					threadLocal.set(connection);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			logger.error("Start trsanction failed!",e);
		}
	}
	
	@Override
	public void commit(){
		try {
			threadLocal.get().commit();
		} catch (SQLException e) {
			logger.error("Commit trsanction failed!",e);
		}
	}
	
	@Override
	public void rollback(){
		try {
			threadLocal.get().rollback();
		} catch (SQLException e) {
			logger.error("Rollback trsanction failed!",e);
		}
	}
	
	@Override
	public void stop() {
		Connection connection = threadLocal.get();
		threadLocal.remove();
		try {
			connection.close();
		} catch (SQLException e) {
			logger.error("Close connection failed!",e);
		}
	}

	public  DataSource getDataSource() {
		return dataSource;
	}

	public  void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public <T> T doInTransaction( Runable<T> runable ) {
		T T = null;
		try {
			this.start();
			T = runable.run();
			this.commit();
		} catch (Exception e) {
			logger.error("Do in transaction failed! roll back this transaction...",e);
			this.rollback();
		}finally{
			this.stop();
		}
		return T;
	}


}
