package com.example.cinema.po;

public class VIPPromotion {
	private int id;
	
	/**
	 * 优惠门槛
	 */
	private int target;
	
	/**
	 * 优惠金额
	 */
	private int discount;
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public int getTarget() {
		return this.target;
	}
	
	public void setTarget(int target) {
		this.target=target;
	}
	
	public int getDiscount() {
		return this.discount;
	}
	
	public void setDiscount(int discount) {
		this.discount=discount;
	}
}
