package com.example.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.BuyLog;
import com.example.model.Product;

@Repository
public class BuyLogRepository {
	
	private final JdbcTemplate jdbcTemplate;
	
	public BuyLogRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<BuyLog> findByAllBuylog() {
		String query = "select * from buylog;";
		List<BuyLog> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(BuyLog.class));

		return list;
	}
	
	//購入履歴登録
	public void insertLog(String username, Product product) {
		String sql = "INSERT INTO buylog (username, productid, price, quantity) VALUES (?, ?, ?, ?)";
		try {
			jdbcTemplate.update(sql, username, product.getId(), product.getPrice(), product.getQuantity());
		} catch (DataAccessException e) {
			// エラーをログに記録
			System.err.println("Error inserting log: " + e.getMessage());
		}
	}

	//ユーザーごとの購入履歴取得
	public List<BuyLog> findByLog(String username) {
		String sql = "SELECT * FROM buylog WHERE username = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BuyLog.class), username);
	}
}
