package com.gaming.live.financial.boundary;

import java.io.Serializable;

import com.gaming.live.common.boundary.ApplicationRequest;

public class Request extends ApplicationRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8192276824346820622L;
	
	private Kaptcha kaptcha;
	
	public Kaptcha getKaptcha() {
		return kaptcha;
	}
	public void setKaptcha(Kaptcha kaptcha) {
		this.kaptcha = kaptcha;
	}
	
}
