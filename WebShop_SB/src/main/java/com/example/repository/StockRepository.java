package com.example.repository;

import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.Product;
import com.example.model.genre;

@Repository
public class StockRepository {
	private final JdbcTemplate jdbcTemplate;
	private final ProductRepository productRepository;

	public StockRepository(JdbcTemplate jdbcTemplate, ProductRepository productRepository) {
		this.jdbcTemplate = jdbcTemplate;
		this.productRepository = productRepository;
	}

	//商品追加
	public void insert(Product product) {
		String query = "insert into product(title,author,release_day,price,stock,quantity,genre,imgLink,notice) values(?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(query, product.getTitle(), product.getAuthor(), product.getRelease_day(),
				product.getPrice(), product.getStock(), 0, product.getGenre(), product.getImgLink(),
				product.getNotice());
	}

	//商品削除
	public void delete(int id) {
		String update = "delete from product where id = ?";
		jdbcTemplate.update(update, id);
	}

	//商品情報更新
	public void updateProduct(Product product) {
		String update = "update product set title = ?,author = ?,release_day= ?,stock = ?,quantity = ?,price = ?,sales = ?,genre = ?,imgLink = ?,recommand = ?,notice = ?,view = ? where id = ?";
		jdbcTemplate.update(update, product.getTitle(), product.getAuthor(), product.getRelease_day(),
				product.getStock(), product.getQuantity(), product.getPrice(), product.getSales(), product.getGenre(),
				product.getImgLink(), product.isRecommand(), product.getNotice(), product.isView(), product.getId());

	}

	//全ジャンル取得
	public List<genre> findAllGenre() {
		String query = "select * from genre";
		return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(genre.class));
	}

	//ジャンル追加
	public void insertGenre(String genre, int id) {
		try {
			if (!genre.isBlank() && !genre.isEmpty()) {
				String update = "insert into genre values(?)";
				jdbcTemplate.update(update, genre);
				String change = "update product set genre = ? where id = ?";
				jdbcTemplate.update(change, genre, id);
			}
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
		}
	}

	//ジャンル削除
	public void deleteGenre(String genre, int id) {
		Product product = productRepository.findById(id);
		if (product.getGenre().equals(genre)) {
			product.setGenre("---");
			updateProduct(product);
		}
		String delete = "delete from genre where genrename = ?";
		jdbcTemplate.update(delete, genre);
	}

	//イチオシを10個に制限
	public boolean CountRecommand() {

		String query = "select count(*) from product where recommand = true";
		int result = jdbcTemplate.queryForObject(query, Integer.class);

		if (result < 10) {
			return true;
		} else {
			return false;
		}
	}

}
