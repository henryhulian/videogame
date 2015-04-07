package com.gaming.live.financial;

public class Application {
	
	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		applicationContext = new ApplicationContext("file:config/config.properties");
		applicationContext.start();
	}
}
