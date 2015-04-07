package com.gaming.live.uc.boundary;

import java.io.Serializable;

import com.gaming.live.common.boundary.ApplicationRequest;
import com.gaming.live.uc.entity.User;

public class Request extends ApplicationRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8192276824346820622L;
	
	private Kaptcha kaptcha;
	private User user;
	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public Kaptcha getKaptcha() {
		return kaptcha;
	}
	public void setKaptcha(Kaptcha kaptcha) {
		this.kaptcha = kaptcha;
	}
	
}
