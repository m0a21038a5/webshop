package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.ShopLocation;

@Repository
public class ShopLocationRepository {
	
	private final JdbcTemplate jdbcTemplate;
	
	public ShopLocationRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	// 支店情報表示
	public List<ShopLocation> shoplocationGetRepo() {
		List<ShopLocation> shoploca = new ArrayList<>();
		String sql = "SELECT * FROM shoplocation";
		shoploca = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ShopLocation.class));
		return shoploca;
	}
}
