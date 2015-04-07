package com.gaming.live.uc;

public class Application {
	
	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		applicationContext = new ApplicationContext("file:config/config.properties");
		applicationContext.start();
	}
}
