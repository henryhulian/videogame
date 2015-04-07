package com.gaming.live.common.entity;

import java.util.Date;
import java.util.List;

public class Session {
	
	private String token;
	private String tokenKey;
	private Integer status;
	private Integer userId;
	private String userName;
	private List<String> roles;
	private Date lastAccessTime=new Date();
	private Date createTime=new Date();
	
	public List<String> getRoles() {
		return roles;
	}
	
	public String getRolesString() {
		return roles.toString().replace("[", "").replace("]", "");
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenKey() {
		return tokenKey;
	}

	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}
	
	@Override
	public String toString() {
		return "Session [token=" + token + ", userId=" + userId + ", userName="
				+ userName + ", roles=" + roles
				+ ", lastAccessTime=" + lastAccessTime + ", createTime="
				+ createTime + "]";
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
