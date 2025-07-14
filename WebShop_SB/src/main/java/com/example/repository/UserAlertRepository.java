package com.example.repository;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.alert_review;
import com.example.service.MailService;
import com.example.service.UserService;

@Repository
public class UserAlertRepository {

	private final JdbcTemplate jdbcTemplate;
	private final UserService userService;
	private final MailService mailService;

	public UserAlertRepository(JdbcTemplate jdbcTemplate, UserService userService, MailService mailService) {
		this.jdbcTemplate = jdbcTemplate;
		this.userService = userService;
		this.mailService = mailService;
	}

	public List<alert_review> UserName_AlertReviewRepo(String user_send_alert) {
		String sql = "select * from alert_review where user_send_alert=?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(alert_review.class), user_send_alert);
	}

	//alert_reviewに通報者と通報されたユーザーの組み合わせがあるか確認する
	public Integer Search_alert_countRepo(String user_send_alert, String username) {
		String sql = "select count(*) from alert_review where user_send_alert=? and user_alert=?";
		int CountAlert = jdbcTemplate.queryForObject(sql, Integer.class, user_send_alert, username);

		if (CountAlert == 0 && !(user_send_alert.equals(username))) {
			String insertSql = "INSERT INTO alert_review(user_send_alert,user_alert) VALUES(?,?)";
			jdbcTemplate.update(insertSql, user_send_alert, username);
		}
		return CountAlert;
	}

	//通報
	public void ReviewAlertRepo(int user_alert_count, String username) {
		String update = "update users set alert = ? where username = ?";
		jdbcTemplate.update(update, user_alert_count, username);

		//5回通報されたらアカウント凍結
		String sql = "SELECT alert FROM users where username=?";
		int CountAlert_frozen = jdbcTemplate.queryForObject(sql, Integer.class, username);
		if (CountAlert_frozen >= 5) {
			String CountAlert_frozen_up = "update users set enabled = ? where username = ?";
			jdbcTemplate.update(CountAlert_frozen_up, true, username);
			mailService.setRegisterMessage(userService.findByUsername(username).getMailaddress(), "品川書店アカウント凍結",
					"あなたのアカウントが凍結されました。");
		}
	}
}
