package com.gaming.live.financial;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gaming.live.common.AbstractApplicationContext;
import com.gaming.live.common.server.ApplicationServer;
import com.gaming.live.financial.boundary.Request;
import com.gaming.live.financial.boundary.Response;
import com.gaming.live.financial.controller.BalanceController;
import com.gaming.live.financial.controller.ControllerDispatcher;
import com.gaming.live.financial.dao.WalletDao;
import com.gaming.live.financial.dao.impl.WalletDaoImpl;
import com.gaming.live.financial.service.BalanceService;
import com.gaming.live.financial.service.impl.BalanceServiceImpl;
import com.google.common.cache.Cache;

public class ApplicationContext extends AbstractApplicationContext{
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);
	
	private WalletDao walletDao;
	
	private BalanceService balanceService;
	
	private BalanceController balanceController;
	
	private ControllerDispatcher controllerDispatcher;
	
	private ApplicationServer< Request, Response> applicationServer;
	
	public ApplicationContext( String path ) {
		
		super(path);
		
		walletDao = new WalletDaoImpl(queryRunner,transactionManagerDataCenter);
		
		balanceService = new BalanceServiceImpl(transactionManagerDataCenter,walletDao);
		
		balanceController = new BalanceController(balanceService);
		
		controllerDispatcher=new ControllerDispatcher(filter,roleService,balanceController);
		
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

}
