package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.Product;
import com.example.service.ProductService;

@Repository
public class SearchRepository {

	private final JdbcTemplate jdbcTemplate;
	private final ProductService productService;

	public SearchRepository(JdbcTemplate jdbcTemplate, ProductService productService) {
		this.jdbcTemplate = jdbcTemplate;
		this.productService = productService;
	}

	//ユーザーページ検索
	public List<Product> searchByKeyword(String keyword, String genre) {
		if ((keyword == null || keyword.isBlank()) && genre.equals("---")) {
			return productService.findViewAll();
		}else if((keyword == null || keyword.isBlank()) && !genre.equals("---")) {
			String query = "SELECT * FROM product WHERE genre = ?";
			return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Product.class),genre);
		}

		List<Product> results = searchByFullText(keyword, genre);

		// 空なら fallback
		if (results.isEmpty()) {
			return searchByOneWord(keyword,genre);
		}

		return results;
	}

	//全文検索
	public List<Product> searchByFullText(String keyword, String genre) {
		// FULLTEXT検索
		StringBuilder fulltextSql = new StringBuilder(
				"""
						    SELECT
						        p.*,
						        (
						            2 * MATCH(p.title) AGAINST (? IN NATURAL LANGUAGE MODE) +
						            1.5 * MATCH(p.author) AGAINST (? IN NATURAL LANGUAGE MODE) +
						            0.5 * MATCH(p.notice) AGAINST (? IN NATURAL LANGUAGE MODE) +
						            1.5 * MATCH(r.title_hira, r.title_kana, r.title_romaji) AGAINST (? IN NATURAL LANGUAGE MODE) +
						            1.5 * MATCH(r.author_hira, r.author_kana, r.author_romaji) AGAINST (? IN NATURAL LANGUAGE MODE)
						        ) AS relevance_score
						    FROM product p
						    LEFT JOIN readings r ON p.id = r.product_id
						    WHERE (
						        MATCH(p.title) AGAINST (? IN NATURAL LANGUAGE MODE) OR
						        MATCH(p.author) AGAINST (? IN NATURAL LANGUAGE MODE) OR
						        MATCH(p.notice) AGAINST (? IN NATURAL LANGUAGE MODE) OR
						        MATCH(r.title_hira, r.title_kana, r.title_romaji) AGAINST (? IN NATURAL LANGUAGE MODE) OR
						        MATCH(r.author_hira, r.author_kana, r.author_romaji) AGAINST (? IN NATURAL LANGUAGE MODE)
						    )
						    AND p.view = true
						""");

		List<Object> params = new ArrayList<>(List.of(
				keyword, keyword, keyword,
				keyword, keyword,
				keyword, keyword, keyword, keyword, keyword));

		if (!genre.equals("---")) {
			fulltextSql.append(" AND p.genre = ?");
			params.add(genre);
		}

		fulltextSql.append(" ORDER BY relevance_score DESC");

		return jdbcTemplate.query(fulltextSql.toString(), new BeanPropertyRowMapper<>(Product.class), params.toArray());

	}

	//一文字ずつ検索
	public List<Product> searchByOneWord(String keyword, String genre) {
		char[] chars = keyword.toCharArray();
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();

		sql.append("SELECT DISTINCT p.*, (");

		for (int i = 0; i < chars.length; i++) {
			if (i > 0)
				sql.append(" + ");
			sql.append("(");
			for (int j = 0; j < 9; j++) { // genreを除く（j=2を飛ばす）
				int fieldIndex = j < 2 ? j : j + 1;
				sql.append("CASE WHEN ").append(jToField(fieldIndex)).append(" LIKE ? THEN ")
						.append(jToScore(fieldIndex)).append(" ELSE 0 END + ");
			}
			sql.setLength(sql.length() - 3); // remove last " + "
			sql.append(")");

			// 各文字に対して LIKE を9カ所（genreを除く）に適用
			for (int j = 0; j < 9; j++) {
				int fieldIndex = j < 2 ? j : j + 1;
				params.add("%" + chars[i] + "%");
			}
		}

		sql.append(
				") AS relevance_score FROM product p LEFT JOIN readings r ON p.id = r.product_id WHERE p.view = true");

		// AND句で genre をフィルタ（検索スコアには関与させない）
		if (!genre.equals("---")) {
			sql.append(" AND p.genre = ?");
			params.add(genre);
		}

		sql.append(" HAVING relevance_score > 0 ORDER BY relevance_score DESC");

		return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Product.class), params.toArray());

	}

	private String jToField(int j) {
		return switch (j) {
		case 0 -> "p.title";
		case 1 -> "p.author";
		case 2 -> "p.genre"; // ←使われていない（除外されてる）
		case 3 -> "p.notice";
		case 4 -> "r.title_hira";
		case 5 -> "r.title_kana";
		case 6 -> "r.title_romaji";
		case 7 -> "r.author_hira";
		case 8 -> "r.author_kana";
		case 9 -> "r.author_romaji";
		default -> "";
		};
	}

	private int jToScore(int j) {
		return switch (j) {
		case 0 -> 3;
		case 1 -> 2;
		case 2 -> 1;
		case 3 -> 1;
		case 4 -> 3;
		case 5 -> 2;
		case 6 -> 1;
		case 7 -> 2;
		case 8 -> 2;
		case 9 -> 1;
		default -> 0;
		};
	}

	//在庫検索
	public List<Product> findSearchAll(int id, String title, String author, String genre) {
		List<Product> list = new ArrayList<>();
		boolean first = false;

		String query = "select * from product where ";

		if (id != 0) {
			query = query + "id = " + id;
			first = true;
		}

		if (!title.isBlank() && !title.isEmpty()) {
			if (!first) {
				query = query + " title like '%" + title + "%'";
				first = true;
			} else {
				query = query + " and title like '%" + title + "%'";
			}

		}

		if (!author.isBlank() && !author.isEmpty()) {
			if (!first) {
				query = query + " author like '%" + author + "%'";
				first = true;
			} else {
				query = query + " and author like '%" + author + "%'";
			}
		}

		if (!genre.equals("---")) {
			if (!first) {
				query = query + " genre = " + "'" + genre + "'";
				first = true;
			} else {
				query = query + " and genre = " + "'" + genre + "'";
			}
		}

		if (first) {
			list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Product.class));
		} else {
			list = null;
		}

		return list;
	}
}
