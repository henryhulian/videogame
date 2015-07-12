package com.gaming.live.uc;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gaming.live.common.AbstractApplicationContext;
import com.gaming.live.common.boundary.ApplicationRequest;
import com.gaming.live.common.boundary.ApplicationResponse;
import com.gaming.live.common.controller.core.Filter;
import com.gaming.live.common.dao.SessionDao;
import com.gaming.live.common.server.ApplicationServer;
import com.gaming.live.common.service.SessionService;
import com.gaming.live.common.transaction.TransactionManager;
import com.gaming.live.common.transaction.TransactionManagerImpl;
import com.gaming.live.uc.boundary.Request;
import com.gaming.live.uc.boundary.Response;
import com.gaming.live.uc.controller.ControllerDispatcher;
import com.gaming.live.uc.controller.authentication.AuthenticationController;
import com.gaming.live.uc.controller.balance.BalanceController;
import com.gaming.live.uc.controller.kaptcha.KaptchaController;
import com.gaming.live.uc.controller.register.RegisterController;
import com.gaming.live.uc.dao.UserDao;
import com.gaming.live.uc.dao.WalletDao;
import com.gaming.live.uc.dao.impl.UserDaoImpl;
import com.gaming.live.uc.dao.impl.WalletDaoImpl;
import com.gaming.live.uc.service.BalanceService;
import com.gaming.live.uc.service.LoginService;
import com.gaming.live.uc.service.RegisterService;
import com.gaming.live.uc.service.impl.BalanceServiceImpl;
import com.gaming.live.uc.service.impl.LoginServiceImpl;
import com.gaming.live.uc.service.impl.RegisterServiceImpl;
import com.google.common.cache.Cache;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

public class ApplicationContext extends AbstractApplicationContext{
	

	private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);
	
	private DataSource dataSource;
	private TransactionManager transactionManager;
	
	private UserDao userDao;
	private WalletDao walletDao;
	
	private RegisterService registerService;
	private LoginService loginService;
	private BalanceService balanceService;
	
	private RegisterController registerController;
	private KaptchaController kaptchaController;
	private AuthenticationController authenticationController;
	private BalanceController balanceController;

	private ControllerDispatcher controllerDispatcher;
	
	private ApplicationServer< Request, Response> applicationServer;
	
	public ApplicationContext( String path ) {
		
		super(path);
		
		//DataSource
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
		
		walletDao = new WalletDaoImpl(queryRunner,transactionManager);
		
		balanceService = new BalanceServiceImpl(transactionManager, walletDao);
		
		balanceController = new BalanceController(balanceService);
		
		userDao = new UserDaoImpl(queryRunner,super.transactionManagerDataCenter);
		
		registerService = new RegisterServiceImpl(transactionManagerDataCenter,userDao);
		loginService = new LoginServiceImpl(transactionManagerDataCenter,userDao);

		
		//Controller
		registerController= new RegisterController(registerService, kaptchaCache);
		kaptchaController=new KaptchaController(kaptchaCache);
		authenticationController=new AuthenticationController(sessionService,loginService);
		
		controllerDispatcher = new ControllerDispatcher(filter,roleService,
				kaptchaController,
				authenticationController,
				registerController,
				balanceController);
		
		//Server
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

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public WalletDao getWalletDao() {
		return walletDao;
	}

	public void setWalletDao(WalletDao walletDao) {
		this.walletDao = walletDao;
	}

	public RegisterService getRegisterService() {
		return registerService;
	}

	public void setRegisterService(RegisterService registerService) {
		this.registerService = registerService;
	}

	public Cache<String, String> getKaptchaCache() {
		return kaptchaCache;
	}

	public void setKaptchaCache(Cache<String, String> kaptchaCache) {
		this.kaptchaCache = kaptchaCache;
	}

	public RegisterController getRegisterController() {
		return registerController;
	}

	public void setRegisterController(RegisterController registerController) {
		this.registerController = registerController;
	}

	public KaptchaController getKaptchaController() {
		return kaptchaController;
	}

	public void setKaptchaController(KaptchaController kaptchaController) {
		this.kaptchaController = kaptchaController;
	}

	public Filter<ApplicationRequest, ApplicationResponse> getFilter() {
		return filter;
	}

	public void setFilter(Filter<ApplicationRequest, ApplicationResponse> filter) {
		this.filter = filter;
	}

	public LoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
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

	public BalanceService getBalanceService() {
		return balanceService;
	}

	public void setBalanceService(BalanceService balanceService) {
		this.balanceService = balanceService;
	}
	
}
