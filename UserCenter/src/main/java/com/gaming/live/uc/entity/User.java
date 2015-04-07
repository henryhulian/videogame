package com.gaming.live.uc.entity;

import java.io.Serializable;
import java.util.Date;


public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2328509210288164373L;
	private Integer id;
	private String userName;
	private String password;
	private String roles;
	private String section;
	private Date createTime = new Date();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "User [userName=" + userName + ", password=" + password
				+ ", roles=" + roles + ", createTime=" + createTime + "]";
	}
	
}
