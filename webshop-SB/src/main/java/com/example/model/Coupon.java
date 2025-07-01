package com.example.model;

public class Coupon {
	private int id;
	private String name;
	private boolean usedcoupon;
	private int discount;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isUsedcoupon() {
		return usedcoupon;
	}
	public void setUsedcoupon(boolean usedcoupon) {
		this.usedcoupon = usedcoupon;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}

}
