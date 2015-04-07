package com.gaming.live.common.boundary;

import java.io.Serializable;

public class ApplicationResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9130003808483092403L;
	
	private int code=200;
	private String message="SUCCESS";
	private String token;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
