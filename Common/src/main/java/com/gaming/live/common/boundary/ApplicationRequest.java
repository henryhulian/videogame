package com.gaming.live.common.boundary;

import java.io.Serializable;

import com.gaming.live.common.entity.Session;


public class ApplicationRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8192276824346820622L;
	
	private int eventId;
	private String token;
	private Session session;
	private String eventName;
	private boolean async = false;
	
	
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public boolean getAsync() {
		return async;
	}
	public void setAsync(boolean async) {
		this.async = async;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	
}
