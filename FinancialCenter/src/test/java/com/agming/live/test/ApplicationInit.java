package com.agming.live.test;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;

import com.gaming.live.common.utils.SqlRunnerUtil;
import com.gaming.live.financial.ApplicationContext;

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
			sqlRunner.runScript(new InputStreamReader(ApplicationInit.class.getClassLoader().getResourceAsStream("init.sql")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//applicationContext.start();
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
