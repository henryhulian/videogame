package com.gaming.live.financial.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Wallet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6879037191315741971L;
	
	private Integer userId;
	private BigDecimal balance;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "Wallet [userId=" + userId + ", balance=" + balance + "]";
	}
	
}
