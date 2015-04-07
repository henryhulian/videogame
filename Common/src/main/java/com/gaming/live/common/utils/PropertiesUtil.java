package com.gaming.live.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gaming.live.common.server.ApplicationServer;

public class PropertiesUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

	public static Properties loadProperties( String path ){
		InputStream input;
		try {
			if(path.startsWith("classpath")){
				input=ApplicationServer.class.getResourceAsStream(path.substring(path.indexOf(":")+1,path.length()));
			}else{
				input = new FileInputStream(new File(path.substring(path.indexOf(":")+1,path.length())));
			}
		} catch (FileNotFoundException e) {
			logger.error("Cannot read properties file",e);
			return null;
		}
		Properties properties = new Properties();
		try {
			properties.load(input);
		} catch (IOException e) {
			logger.error("Cannot load properties file");
			return null;
		}
		return properties;
	}
}
