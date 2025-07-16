package com.example.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.SearchLog;

@Repository
public class SearchLogRepository {
	
	private final JdbcTemplate jdbcTemplate;
	
	public SearchLogRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void saveSearchLog(SearchLog searchlog) {

		int result = 0;

		//id重複確認
		if (searchlog.getId() != 0) {
			String count = "SELECT COUNT(*) FROM searchlog WHERE id = ? and username = ? and place = ?";
			result = jdbcTemplate.queryForObject(count, Integer.class, searchlog.getId(), searchlog.getUsername(),
					searchlog.getPlace());

			if (result > 0) {
				String update = "UPDATE searchlog SET date = ? WHERE id = ? and username = ? and place = ?";
				jdbcTemplate.update(update, LocalDateTime.now(), searchlog.getId(), searchlog.getUsername(),
						searchlog.getPlace());
			}
		}

		//タイトル重複確認
		if (searchlog.getTitle() != null) {
			String count = "SELECT COUNT(*) FROM searchlog WHERE title = ? and username = ? and place = ?";
			result = jdbcTemplate.queryForObject(count, Integer.class, searchlog.getTitle(), searchlog.getUsername(),
					searchlog.getPlace());

			if (result > 0) {
				String update = "UPDATE searchlog SET date = ? WHERE title = ? and username = ? and place = ?";
				jdbcTemplate.update(update, LocalDateTime.now(), searchlog.getTitle(), searchlog.getUsername(),
						searchlog.getPlace());
			}
		}

		//著者重複確認
		if (searchlog.getAuthor() != null) {
			String count = "SELECT COUNT(*) FROM searchlog WHERE author = ? and username = ? and place = ?";
			result = jdbcTemplate.queryForObject(count, Integer.class, searchlog.getAuthor(), searchlog.getUsername(),
					searchlog.getPlace());

			if (result > 0) {
				String update = "UPDATE searchlog SET date = ? WHERE author = ? and username = ? and place = ?";
				jdbcTemplate.update(update, LocalDateTime.now(), searchlog.getAuthor(), searchlog.getUsername(),
						searchlog.getPlace());
			}
		}

		//ジャンル重複確認
		if (searchlog.getGenre() != null) {
			String count = "SELECT COUNT(*) FROM searchlog WHERE genre = ? and username = ? and place = ?";
			result = jdbcTemplate.queryForObject(count, Integer.class, searchlog.getGenre(), searchlog.getUsername(),
					searchlog.getPlace());

			if (result > 0) {
				String update = "UPDATE searchlog SET date = ? WHERE genre = ? and username = ? and place = ?";
				jdbcTemplate.update(update, LocalDateTime.now(), searchlog.getGenre(), searchlog.getUsername(),
						searchlog.getPlace());
			}
		}

		//重複なしなら追加
		if (result == 0) {
			String insert = "INSERT INTO searchlog(username,place,id,title,author,genre) VALUES(?,?,?,?,?,?)";
			jdbcTemplate.update(insert, searchlog.getUsername(), searchlog.getPlace(), searchlog.getId(),
					searchlog.getTitle(), searchlog.getAuthor(), searchlog.getGenre());
		}
	}

	//在庫管理ページでのid検索欄用の検索履歴
	public List<SearchLog> findSearchIdLog(String username, String place) {
		String query = "SELECT * FROM searchlog WHERE username = ? and place = ? and id != 0 ORDER BY date DESC LIMIT 5";
		List<SearchLog> list = new ArrayList<>();
		list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(SearchLog.class), username, place);
		return list;
	}

	//在庫管理ページでのタイトル検索欄用の検索履歴
	public List<SearchLog> findSearchTitleLog(String username, String place) {
		String query = "SELECT * FROM searchlog WHERE username = ? and place = ? and title != ? ORDER BY date DESC LIMIT 5";
		List<SearchLog> list = new ArrayList<>();
		list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(SearchLog.class), username, place, "null");
		return list;
	}

	//在庫管理ページでの著者検索欄用の検索履歴
	public List<SearchLog> findSearchAuthorLog(String username, String place) {
		String query = "SELECT * FROM searchlog WHERE username = ? and place = ? and author != ? ORDER BY date DESC LIMIT 5";
		List<SearchLog> list = new ArrayList<>();
		list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(SearchLog.class), username, place, "null");
		return list;
	}

	//在庫管理ページでのジャンル検索欄用の検索履歴
	public List<SearchLog> findSearchGenreLog(String username, String place) {
		String query = "SELECT * FROM searchlog WHERE username = ? and place = ? and genre != null ORDER BY date DESC LIMIT 5";
		List<SearchLog> list = new ArrayList<>();
		list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(SearchLog.class), username, place);
		return list;
	}
	
}
