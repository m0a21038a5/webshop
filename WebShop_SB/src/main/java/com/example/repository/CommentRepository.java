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

	public List<Comment> findAllReview() {
		String query = "SELECT * FROM comment";
		return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Comment.class));
	}

	// コメントをIDで取得
	public Comment findByReviewId(int product_id) {
		String query = "SELECT * FROM comment WHERE product_id = ?";
		List<Comment> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Comment.class), product_id);
		return list.isEmpty() ? null : list.get(0);
	}

	// レビューをデータベースに追加
	public void addReview(int productId, int userId, String reviewText) {
		String sql = "INSERT INTO comment(product_id, user_id, comment) VALUES(?, ?, ?)";
		jdbcTemplate.update(sql, productId, userId, reviewText);
	}

	// レビューを更新
	public void updateReview(int commentId, int userId, String updatedReview) {
		String sql = "UPDATE comment SET user_id = ?, comment = ? WHERE id = ?";
		jdbcTemplate.update(sql, userId, updatedReview, commentId);
	}

}