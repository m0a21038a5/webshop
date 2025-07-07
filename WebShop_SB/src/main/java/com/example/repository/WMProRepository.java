package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.BeforeProducts;
import com.example.model.BuyLog;
import com.example.model.Product;

@Repository
public class WMProRepository {

	private final JdbcTemplate jdbcTemplate;

	public WMProRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	public List<BuyLog> findByAllBuylog() {
		String query = "select * from buylog;";
		List<BuyLog> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(BuyLog.class));

		return list;
	}

	//既存価格を変更する
	public void updateprice(int price, String title) {
		String query = "update product set price = ? where title = ?;";
		jdbcTemplate.update(query, price, title);

	}

	//既存価格をバックアップ
	public void backupPrice() {
		String query = "insert into beforeproducts (name,price) select title,price from Product;";
		jdbcTemplate.update(query);

	}

	//タイムセール終了時に商品価格を元に戻す
	public void finishedPrice(int price, String title) {
		String query = "update product set price = ? where title = ?; ";
		jdbcTemplate.update(query, price, title);
	}

	//バックアップした商品情報をすべて選択
	public List<BeforeProducts> findAllBeforeProduct() {
		List<BeforeProducts> list = new ArrayList<>();
		String query = "select * from beforeproducts";
		list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(BeforeProducts.class));

		return list;
	}

	//バックアップした価格を更新
	public void updateBackupPrice(Product product) {
		String query = "update beforeproducts set price = ? where name = ?;";
		jdbcTemplate.update(query, product.getPrice(), product.getTitle());
	}

}
