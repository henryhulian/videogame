package com.gaming.live.uc.boundary;

import java.io.Serializable;

import com.gaming.live.common.boundary.ApplicationResponse;

public class Response extends ApplicationResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9130003808483092403L;
	
	private Kaptcha kaptcha;
	
	public Kaptcha getKaptcha() {
		return kaptcha;
	}
	public void setKaptcha(Kaptcha kaptcha) {
		this.kaptcha = kaptcha;
	}
	
	
}
