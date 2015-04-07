package com.gaming.live.financial;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gaming.live.common.server.ApplicationServer;
import com.gaming.live.common.transaction.TransactionManager;
import com.gaming.live.common.transaction.TransactionManagerImpl;
import com.gaming.live.common.utils.PropertiesUtil;
import com.gaming.live.financial.boundary.Request;
import com.gaming.live.financial.boundary.Response;
import com.gaming.live.financial.controller.ControllerDispatcher;
import com.gaming.live.financial.dao.WalletDao;
import com.gaming.live.financial.dao.impl.WalletDaoImpl;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

public class ApplicationContext{
	

	private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);
	
	private DataSource dataSource;
	private TransactionManager transactionManager;
	private QueryRunner queryRunner;
	
	private WalletDao walletDao;
	
	
	private Cache<String, String> kaptchaCache;
	private Map<String, String[]> roleMap = new HashMap<String, String[]>();

	private ControllerDispatcher controllerDispatcher;
	
	private ApplicationServer< Request, Response> applicationServer;
	
	public ApplicationContext( String path ) {
		Properties properties = PropertiesUtil.loadProperties(path);
		DbUtils.loadDriver(properties.getProperty("datasource.jdbc.driver"));
		BoneCPConfig boneCPConfig = new BoneCPConfig();
		boneCPConfig.setJdbcUrl(properties.getProperty("datasource.jdbc.url"));
		boneCPConfig.setUsername(properties.getProperty("datasource.jdbc.username"));
		boneCPConfig.setPassword(properties.getProperty("datasource.jdbc.password"));
		boneCPConfig.setIdleConnectionTestPeriodInMinutes(5);
		boneCPConfig.setIdleMaxAgeInMinutes(10);
		boneCPConfig.setMaxConnectionsPerPartition(Integer.parseInt(properties.getProperty("datasource.jdbc.poolsize")));
		boneCPConfig.setMinConnectionsPerPartition(10);
		boneCPConfig.setPartitionCount(3);
		boneCPConfig.setAcquireIncrement(5);
		boneCPConfig.setStatementsCacheSize(100);
		boneCPConfig.setDisableConnectionTracking(true);
		dataSource = new BoneCPDataSource(boneCPConfig);
		
		transactionManager = new TransactionManagerImpl(dataSource);
		queryRunner = new QueryRunner();
		
		walletDao = new WalletDaoImpl(queryRunner,transactionManager);
		
		
		kaptchaCache = CacheBuilder.newBuilder().maximumSize(10000).build();
		
		applicationServer = new ApplicationServer<>(properties,controllerDispatcher, Request.class);
	}

	public void start(){
		logger.info("Start application");
		applicationServer.start();
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public TransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public QueryRunner getQueryRunner() {
		return queryRunner;
	}

	public void setQueryRunner(QueryRunner queryRunner) {
		this.queryRunner = queryRunner;
	}

	public WalletDao getWalletDao() {
		return walletDao;
	}

	public void setWalletDao(WalletDao walletDao) {
		this.walletDao = walletDao;
	}

	public Cache<String, String> getKaptchaCache() {
		return kaptchaCache;
	}

	public void setKaptchaCache(Cache<String, String> kaptchaCache) {
		this.kaptchaCache = kaptchaCache;
	}

	public Map<String, String[]> getRoleMap() {
		return roleMap;
	}

	public void setRoleMap(Map<String, String[]> roleMap) {
		this.roleMap = roleMap;
	}
	
}
