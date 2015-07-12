package com.gaming.live.common;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import com.gaming.live.common.boundary.ApplicationRequest;
import com.gaming.live.common.boundary.ApplicationResponse;
import com.gaming.live.common.controller.core.Filter;
import com.gaming.live.common.controller.core.impl.SecurityFilter;
import com.gaming.live.common.dao.SessionDao;
import com.gaming.live.common.dao.impl.SessionDaoImpl;
import com.gaming.live.common.entity.Session;
import com.gaming.live.common.service.RoleService;
import com.gaming.live.common.service.SessionService;
import com.gaming.live.common.service.impl.RoleServiceImpl;
import com.gaming.live.common.service.impl.SessionServiceImpl;
import com.gaming.live.common.transaction.TransactionManager;
import com.gaming.live.common.transaction.TransactionManagerImpl;
import com.gaming.live.common.utils.PropertiesUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

abstract public class  AbstractApplicationContext {

	protected Properties properties;
	protected DataSource dataSource;
	protected DataSource dataSourceDataCenter;
	protected TransactionManager transactionManagerDataCenter;
	protected QueryRunner queryRunner;
	
	protected SessionDao sessionDao;
	
	protected RoleService roleService;
	protected SessionService sessionService;
	
	protected Cache<String, String> kaptchaCache;
	protected Cache<String,Session> sessionCache;

	protected Filter<ApplicationRequest, ApplicationResponse> filter;
	
	public AbstractApplicationContext( String path ) {
		
		properties = PropertiesUtil.loadProperties(path);
		DbUtils.loadDriver(properties.getProperty("datasource.jdbc.driver"));
		queryRunner = new QueryRunner();
		
		//Cache
		kaptchaCache = CacheBuilder.newBuilder().maximumSize(10000).build();
		sessionCache = CacheBuilder.newBuilder().maximumSize(10000).expireAfterAccess(2, TimeUnit.HOURS).build();
		
		//Server Service
		roleService=new RoleServiceImpl();
		
		
		//DataSource DataCenter
		BoneCPConfig boneCPConfigDataCenter = new BoneCPConfig();
		boneCPConfigDataCenter.setJdbcUrl(properties.getProperty("dataSourceDataCenter.jdbc.url"));
		boneCPConfigDataCenter.setUsername(properties.getProperty("dataSourceDataCenter.jdbc.username"));
		boneCPConfigDataCenter.setPassword(properties.getProperty("dataSourceDataCenter.jdbc.password"));
		boneCPConfigDataCenter.setIdleConnectionTestPeriodInMinutes(5);
		boneCPConfigDataCenter.setIdleMaxAgeInMinutes(10);
		boneCPConfigDataCenter.setMaxConnectionsPerPartition(Integer.parseInt(properties.getProperty("dataSourceDataCenter.jdbc.poolsize")));
		boneCPConfigDataCenter.setMinConnectionsPerPartition(10);
		boneCPConfigDataCenter.setPartitionCount(3);
		boneCPConfigDataCenter.setAcquireIncrement(5);
		boneCPConfigDataCenter.setStatementsCacheSize(100);
		boneCPConfigDataCenter.setDisableConnectionTracking(true);
		dataSourceDataCenter = new BoneCPDataSource(boneCPConfigDataCenter);
		transactionManagerDataCenter = new TransactionManagerImpl(dataSourceDataCenter);
		
		sessionDao = new SessionDaoImpl(transactionManagerDataCenter, queryRunner);
		
		sessionService=new SessionServiceImpl(sessionCache,transactionManagerDataCenter,sessionDao);
		
		
		filter = new SecurityFilter(roleService,sessionService);
	}


	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public QueryRunner getQueryRunner() {
		return queryRunner;
	}

	public void setQueryRunner(QueryRunner queryRunner) {
		this.queryRunner = queryRunner;
	}


	public Cache<String, String> getKaptchaCache() {
		return kaptchaCache;
	}

	public void setKaptchaCache(Cache<String, String> kaptchaCache) {
		this.kaptchaCache = kaptchaCache;
	}


	public Filter<ApplicationRequest, ApplicationResponse> getFilter() {
		return filter;
	}

	public void setFilter(Filter<ApplicationRequest, ApplicationResponse> filter) {
		this.filter = filter;
	}

	public DataSource getDataSourceDataCenter() {
		return dataSourceDataCenter;
	}

	public void setDataSourceDataCenter(DataSource dataSourceDataCenter) {
		this.dataSourceDataCenter = dataSourceDataCenter;
	}

	public TransactionManager getTransactionManagerDataCenter() {
		return transactionManagerDataCenter;
	}

	public void setTransactionManagerDataCenter(
			TransactionManager transactionManagerDataCenter) {
		this.transactionManagerDataCenter = transactionManagerDataCenter;
	}

	public SessionDao getSessionDao() {
		return sessionDao;
	}

	public void setSessionDao(SessionDao sessionDao) {
		this.sessionDao = sessionDao;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

}
