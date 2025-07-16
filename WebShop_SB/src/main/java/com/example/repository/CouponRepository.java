package com.example.repository;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.Coupon;

@Repository
public class CouponRepository {

	private final JdbcTemplate jdbcTemplate;

	public CouponRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	//クーポン
	public Coupon findByCoupon(String couponcode) {
		String query = "SELECT * FROM coupon where name = ?";
		List<Coupon> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Coupon.class), couponcode);
		return list.isEmpty() ? null : list.get(0);
	}

	//クーポンを使用済みに変更
	public void couponUpdate(String couponcode) {
		String query = "UPDATE coupon SET usedcoupon = true where name = ?";
		System.out.println("couponcode");
		jdbcTemplate.update(query, couponcode);
	}
}
