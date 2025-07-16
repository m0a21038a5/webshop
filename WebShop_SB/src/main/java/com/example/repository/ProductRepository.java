package com.example.repository;

import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.Product;

@Repository
public class ProductRepository {
	private final JdbcTemplate jdbcTemplate;
	final Logger logger = LoggerFactory.getLogger(Product.class);

	public ProductRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	//公開済み商品取得
	public List<Product> findViewAll() {
		String query = "SELECT * FROM product where view = true";
		return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Product.class));
	}

	//イチオシ商品取得
	public List<Product> findRecommand() {
		String query = "SELECT * FROM product where recommand = true";
		return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Product.class));
	}

	//トップ10取得
	public List<Product> findSalesRank() {
		String query = "SELECT * FROM product WHERE view = true ORDER BY sales DESC LIMIT 10;";
		return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Product.class));
	}

	//全商品取得
	public List<Product> findAll() {
		String query = "SELECT * FROM product";
		return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Product.class));
	}

	//id検索
	public Product findById(int id) {
		String query = "SELECT * FROM product WHERE id = ?";
		List<Product> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Product.class), id);
		return list.isEmpty() ? null : list.get(0);
	}

	//ソート
	public void sortProductList(List<Product> products, String sortrule) {
		if ("recommendation".equals(sortrule)) {
			products.sort(Comparator.comparing(Product::getSales).reversed());
		} else if ("favorite".equals(sortrule)) {
			products.sort(Comparator.comparing(Product::getSales));
		} else {
			products.sort(Comparator.comparing(Product::getId));
		}
	}

	//購入処理
	public int update(Product product) {
		int stock = 0;
		int sales = 0;
		String query = "SELECT * FROM product WHERE id = ?";
		List<Product> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Product.class), product.getId());
		if (!list.isEmpty()) {
			stock = list.get(0).getStock() - product.getQuantity();
			sales = product.getSales() + product.getQuantity();
		}
		if (stock < 0) {
			return -1; // 在庫が不足している場合
		}
		String update = "UPDATE product SET stock = ?, sales = ? WHERE id = ?";
		return jdbcTemplate.update(update, stock, sales, product.getId());
	}

}