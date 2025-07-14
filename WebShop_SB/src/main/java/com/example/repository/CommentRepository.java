package com.example.repository;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.Comment;

@Repository
public class CommentRepository {
	private final JdbcTemplate jdbcTemplate;

	public CommentRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	//レビュー全取得
	public List<Comment> findAllReview() {
		String query = "SELECT * FROM comment";
		return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Comment.class));
	}

	// コメントをIDで取得
	public List<Comment> findByReviewId(int product_id) {
		String query = "SELECT * FROM comment WHERE product_id = ?";
		List<Comment> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Comment.class), product_id);
		return list.isEmpty() ? null : list;
	}

	// レビューをデータベースに追加
	public void addReview(int productId, String username, String reviewText) {
		String sql = "INSERT INTO comment(product_id, user_name, comment) VALUES(?, ?, ?)";
		jdbcTemplate.update(sql, productId, username, reviewText);
	}

	// レビューを更新
	public void updateReview(int commentId, String username, String updatedReview) {
		String sql = "UPDATE comment SET user_name = ?, comment = ? WHERE id = ?";
		jdbcTemplate.update(sql, username, updatedReview, commentId);
	}
	
	
	
}