package com.example.session;

import java.util.List;

import com.example.model.Product;

public class StockSession {
	
	private List<Product> stock;
	private String searchId;
	private String searchTitle;
	private String searchAuthor;
	private String searchGenre;

	public List<Product> getStock() {
		return stock;
	}

	public void setStock(List<Product> stock) {
		this.stock = stock;
	}

	public String getSearchId() {
		return searchId;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}

	public String getSearchTitle() {
		return searchTitle;
	}

	public void setSearchTitle(String searchTitle) {
		this.searchTitle = searchTitle;
	}

	public String getSearchAuthor() {
		return searchAuthor;
	}

	public void setSearchAuthor(String searchAuthor) {
		this.searchAuthor = searchAuthor;
	}

	public String getSearchGenre() {
		return searchGenre;
	}

	public void setSearchGenre(String searchGenre) {
		this.searchGenre = searchGenre;
	}
}
