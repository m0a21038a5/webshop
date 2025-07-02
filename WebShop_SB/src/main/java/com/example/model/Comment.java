package com.example.model;
 
public class Comment {
		private int comment_id;
		private int product_id;
		private int user_id;
		private String user_name;
		private String comment;
		private boolean alreadyAlert;

	public Comment() {
	}
 
	public int getComment_id() {
		return comment_id;
	}
 
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
 
	public int getProduct_id() {
		return product_id;
	}
 
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
 
	public int getUser_id() {
		return user_id;
	}
 
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
 
	public String getUser_name() {
		return user_name;
	}
 
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
 
	public String getComment() {
		return comment;
	}
 
	public void setComment(String comment) {
		this.comment = comment;
	}

 
	public boolean isAlreadyAlert() {
		return alreadyAlert;
	}
 
	public void setAlreadyAlert(boolean alreadyAlert) {
		this.alreadyAlert = alreadyAlert;
	}
 
}
