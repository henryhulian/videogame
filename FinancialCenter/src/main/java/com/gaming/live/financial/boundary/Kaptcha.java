package com.gaming.live.financial.boundary;

public class Kaptcha {

	private String kaptchaKey;
	private String kaptchaValue;
	private String imageBytes;
	
	public Kaptcha() {
	}
	
	public Kaptcha(String kaptchaKey, String imageBytes) {
		super();
		this.kaptchaKey = kaptchaKey;
		this.imageBytes = imageBytes;
	}
	
	public String getImageBytes() {
		return imageBytes;
	}
	public void setImageBytes(String imageBytes) {
		this.imageBytes = imageBytes;
	}

	public String getKaptchaKey() {
		return kaptchaKey;
	}

	public void setKaptchaKey(String kaptchaKey) {
		this.kaptchaKey = kaptchaKey;
	}

	public String getKaptchaValue() {
		return kaptchaValue;
	}

	public void setKaptchaValue(String kaptchaValue) {
		this.kaptchaValue = kaptchaValue;
	}
}
