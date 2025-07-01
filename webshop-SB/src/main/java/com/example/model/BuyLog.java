package com.example.model;

import java.time.LocalDateTime;

public class BuyLog {
	private int id;
	private LocalDateTime created_at;  //insertで自動で現在時刻が入る
	private String Time;			   //LocalDateTimeをStringに直すために定義
	private int productid;			//商品id
	private String username;			//購入したユーザー名
	private int price;					//購入した商品の価格
	private int quantity;				//購入した商品の個数
	private String productname;
	
	//以下、セッターとゲッター

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public int getProductid() {
		return productid;
	}

	public void setProductid(int productid) {
		this.productid = productid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	
	
}
