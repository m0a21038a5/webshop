package com.example.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.Readings;

@Repository
public class ReadingRepository {
	private JdbcTemplate jdbcTemplate;

	public ReadingRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void insertReading(int productId, String titleHira, String titleKana, String titleRomaji,
			String authorHira, String authorKana, String authorRomaji) {

		String sql = """
				INSERT INTO readings
				(product_id, title_hira, title_kana, title_romaji, author_hira, author_kana, author_romaji)
				VALUES (?, ?, ?, ?, ?, ?, ?)
				""";

		jdbcTemplate.update(sql, productId, titleHira, titleKana, titleRomaji,
				authorHira, authorKana, authorRomaji);

		insertReading(productId, "たいとる", "タイトル", "taitoru", "ちょしゃ", "チョシャ",
				"tyosya");
	}

	public void updateReading(int productId, String titleHira, String titleKana, String titleRomaji,
			String authorHira, String authorKana, String authorRomaji) {
		String sql = """
				UPDATE readings
				SET title_hira = ?,title_kana = ?, title_romaji = ?, author_hira = ?, author_kana = ?, author_romaji = ?
				WHERE product_id = ?;
				""";
		jdbcTemplate.update(sql, titleHira, titleKana, titleRomaji, authorHira, authorKana, authorRomaji, productId);
	}

	public Readings findByProductId(int product_id) {
		try {
			String sql = "SELECT * FROM readings WHERE product_id = ?;";
			return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Readings.class), product_id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
