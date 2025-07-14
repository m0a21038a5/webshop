package com.example.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.Product;
import com.example.model.ViewLog;
import com.example.service.ProductService;

@Repository
public class ViewLogRepository {
	private final JdbcTemplate jdbcTemplate;
	private final ProductService productService;

	public ViewLogRepository(JdbcTemplate jdbcTemplate, ProductService productService) {
		this.jdbcTemplate = jdbcTemplate;
		this.productService = productService;
	}

	//閲覧履歴保存
	public void addViewLog(String username, int product_id) {
		String check = "SELECT COUNT(*) FROM viewlog WHERE productid = ? and username = ?";
		int result = jdbcTemplate.queryForObject(check, Integer.class, product_id, username);

		if (result == 0) {
			Product pro = productService.findById(product_id);
			String insert = "INSERT INTO viewlog(username,productid,title,author,release_day,price,genre,imgLink) VALUES(?,?,?,?,?,?,?,?)";
			jdbcTemplate.update(insert, username, product_id, pro.getTitle(), pro.getAuthor(), pro.getRelease_day(),
					pro.getPrice(), pro.getGenre(), pro.getImgLink());
		} else if (result > 0) {
			String update = "UPDATE viewlog SET date = ? WHERE productid = ?";
			jdbcTemplate.update(update, LocalDateTime.now(), product_id);
		}
	}

	//閲覧履歴取り出し
	public List<ViewLog> findAllViewLog(String username) {
		List<ViewLog> list = new ArrayList<>();
		String query = "SELECT * FROM viewlog WHERE username = ? ORDER BY date DESC";
		list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(ViewLog.class), username);
		return list;
	}
}
