package com.example.model;

import java.time.LocalDate;

public class Product {
	private int id;
	private String title;
	private String author;
	private String release_day;
	private int price;
	private int quantity;
	private int stock;
	private int sales;
	private String genre;
	private String imgLink;
	private boolean recommand;
	private String notice;
	private boolean view;
	private boolean fabolit;
	private LocalDate releaseDay;
	private int rank;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Product() {
	}

	public int getSales() {
		return sales;
	}

	public void setSales(int sales) {
		this.sales = sales;
	}

	public boolean isView() {
		return view;
	}

	public void setView(boolean view) {
		this.view = view;
	}

	public boolean isRecommand() {
		return recommand;
	}

	public void setRecommand(boolean recommand) {
		this.recommand = recommand;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRelease_day() {
		return release_day;
	}

	public void setRelease_day(String release_day) {
		this.release_day = release_day;
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

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getImgLink() {
		return imgLink;
	}

	public void setImgLink(String imgLink) {
		this.imgLink = imgLink;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public boolean getFabolit() {
		return fabolit; // fabolitの値を返す
	}

	public void setFabolit(boolean fabolit) {
		this.fabolit = fabolit;
	}

	public LocalDate getReleaseDay() {
		return releaseDay;
	}

	public void setReleaseDay(LocalDate releaseDay) {
		this.releaseDay = releaseDay;
	}
}
