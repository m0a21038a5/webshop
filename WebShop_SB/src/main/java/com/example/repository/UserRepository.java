package com.example.repository;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.example.model.User;
import com.example.service.MailService;

@Repository
public class UserRepository {
	private final PasswordEncoder passwordEncoder;
	private final JdbcTemplate jdbcTemplate;

	public UserRepository(JdbcTemplate jdbcTemplate, MailService mailService,
			PasswordEncoder passwordEncoder) {
		this.jdbcTemplate = jdbcTemplate;
		this.passwordEncoder = passwordEncoder;
	}

	//ユーザー名からユーザー検索
	public User findByUsername(String username) {
		String query = "SELECT * FROM users WHERE username = ?";
		List<User> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(User.class), username);
		return list.isEmpty() ? null : list.get(0);
	}

	//新規ユーザー登録
	public int save(User user) {
		int point = 0;
		String query = "INSERT INTO users(username, mailaddress, address, age, password, role, alert, enabled, point) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			return jdbcTemplate.update(query, user.getUsername(), user.getMailaddress(), user.getAddress(),
					user.getAge(), passwordEncoder.encode(user.getPassword()), user.getRole(), user.getAlert(),
					user.isEnabled(), point);
		} catch (Exception e) {
			// エラーログを出力
			System.err.println("Error saving user: " + e.getMessage());
			return -1; // エラーの場合は -1 を返す
		}
	}

	//ユーザーパスワード更新
	public int update(User user) {
		String query = "UPDATE users SET mailaddress = ?, address = ?, age = ?, password = ? WHERE username = ?";
		return jdbcTemplate.update(query, user.getMailaddress(), user.getAddress(), user.getAge(),
				passwordEncoder.encode(user.getPassword()), user.getUsername());
	}

	//アカウント凍結
	public void updateEnabled(User user) {
		String update = "update users set enabled = ? where username = ?";
		jdbcTemplate.update(update, user.isEnabled(), user.getUsername());

		if (!user.isEnabled()) {
			String reset = "update users set alert = 0 where username = ?";
			jdbcTemplate.update(reset, user.getUsername());
		}
	}

	//ポイント 
	public void pointUpdate(User user) {
		String sql = "UPDATE users SET point = ? WHERE id = ?";
		jdbcTemplate.update(sql, user.getPoint(), user.getId());
	}

	//ユーザー全取得
	public List<User> findAllUser() {
		String query = "select * from users";
		return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(User.class));
	}

	//ユーザーロール変更
	public void updateRole(User user) {
		String update = "update users set role = ? where username = ?";
		jdbcTemplate.update(update, user.getRole(), user.getUsername());
	}

	//ユーザー情報更新
	public void updateAll(User user) {
		String query = "UPDATE users SET mailaddress = ?, address = ?, age = ? WHERE username = ?";
		jdbcTemplate.update(query, user.getMailaddress(), user.getAddress(), user.getAge(), user.getUsername());
	}

}