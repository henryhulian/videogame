package com.agming.live.uc.test;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;

import com.gaming.live.common.utils.SqlRunnerUtil;
import com.gaming.live.uc.ApplicationContext;

public class ApplicationInit {
	
	private static ApplicationInit instance = new ApplicationInit();
	public static ApplicationInit getInstance() {
		return instance;
	}
	
	private ApplicationContext applicationContext;
	
	private  ApplicationInit(){
		applicationContext = new ApplicationContext("classpath:/config.properties");
		
		try {
			SqlRunnerUtil sqlRunner = new SqlRunnerUtil(applicationContext.getDataSource().getConnection(),
											new PrintWriter(System.out), 
											new PrintWriter(System.err), 
											false ,
											true);
			sqlRunner.runScript(new InputStreamReader(ApplicationInit.class.getClassLoader().getResourceAsStream("application.sql")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			SqlRunnerUtil sqlRunnerDataCenter = new SqlRunnerUtil(applicationContext.getDataSourceDataCenter().getConnection(),
											new PrintWriter(System.out), 
											new PrintWriter(System.err), 
											false ,
											true);
			sqlRunnerDataCenter.runScript(new InputStreamReader(ApplicationInit.class.getClassLoader().getResourceAsStream("datacenter.sql")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//applicationContext.start();
	}
	
	public static void main(String[] args) {
		ApplicationInit applicationInit = new ApplicationInit();
		applicationInit.getApplicationContext().start();
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
