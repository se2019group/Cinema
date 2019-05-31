package com.example.cinema.vo;

public class PeopleMatched {
	private String userName;
	private double consumeAmount;
	private double chargeAmount;
	private int userId;
	
	public void setUserId(int userId) {
		this.userId=userId;
	}
	
	public int getUserId() {
		return this.userId;
	}
	
	public void setUserName(String userName) {
		this.userName=userName;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public void setConsumeAmount(double consumeAmount) {
		this.consumeAmount=consumeAmount;
	}
	
	public double getConsumeAmount() {
		return this.consumeAmount;
	}
	
	public void setChargeAmount(double chargeAmount) {
		this.chargeAmount=chargeAmount;
	}
	
	public double getChargeAmount() {
		return this.chargeAmount;
	}
}
