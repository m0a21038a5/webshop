package com.example.repository;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.example.model.BuyLog;
import com.example.model.Product;
import com.example.model.User;
import com.example.session.CartSession;

@Repository
public class UserRepository {

	private JavaMailSender javaMailSender;
	private final PasswordEncoder passwordEncoder;
	private final JdbcTemplate jdbcTemplate;

	public UserRepository(JdbcTemplate jdbcTemplate, MailSender mailSender, JavaMailSender javaMailSender,
			PasswordEncoder passwordEncoder) {
		this.jdbcTemplate = jdbcTemplate;
		this.javaMailSender = javaMailSender;
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

	//アドミンユーザーを初期に登録
	public void AddAdmin() {
		try {
			String check = "SELECT count(*) FROM users WHERE username = ?";
			int result = jdbcTemplate.queryForObject(check, Integer.class, "admin");

			if (result == 0) {
				String sql = "INSERT INTO users(username, mailaddress, address, age, password, role, alert, enabled) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
				jdbcTemplate.update(sql, "admin", "admin@persol.co.jp", "新宿", 40, passwordEncoder.encode("password"),
						"ADMIN", 0, false);
			}

		} catch (BadSqlGrammarException e) {
			// エラーが発生した場合は、テーブルを再作成する
			recreateUserTable();
		}
	}

	//Userテーブル作成
	private void recreateUserTable() {
		String drop = "DROP TABLE IF EXISTS users;";
		jdbcTemplate.update(drop);
		String sql = """
				CREATE TABLE users (
				id              INTEGER        AUTO_INCREMENT PRIMARY KEY,
				username        VARCHAR(255)   UNIQUE,
				mailaddress     VARCHAR(255)   UNIQUE NOT NULL,
				address         VARCHAR(255)   NOT NULL,
				age             INTEGER        NOT NULL,
				password        VARCHAR(255)   NOT NULL,
				role            VARCHAR(100)   NOT NULL,
				alert           INTEGER        NOT NULL,
				enabled         BOOLEAN NOT NULL
				);
				""";
		jdbcTemplate.update(sql);

		// adminユーザーを再度追加
		String sqlInsertAdmin = "INSERT INTO users(username, mailaddress, address, age, password, role, alert, enabled) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlInsertAdmin, "admin", "admin@persol.co.jp", "新宿", 40, passwordEncoder.encode("password"),
				"ADMIN", 0, false);
	}

	//購入履歴登録
	public void insertLog(String username, Product product) {
		String sql = "INSERT INTO buylog (username, productid, price, quantity) VALUES (?, ?, ?, ?)";
		try {
			jdbcTemplate.update(sql, username, product.getId(), product.getPrice(), product.getQuantity());
		} catch (DataAccessException e) {
			// エラーをログに記録
			System.err.println("Error inserting log: " + e.getMessage());
		}
	}

	//ユーザーごとの購入履歴取得
	public List<BuyLog> findByLog(String username) {
		String sql = "SELECT * FROM buylog WHERE username = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BuyLog.class), username);
	}

	//ポイント 
	public void pointUpdate(User user) {
		String sql = "UPDATE users SET point = ? WHERE id = ?";
		jdbcTemplate.update(sql, user.getPoint(), user.getId());
	}
	
	//購入完了メール
	public void SendCartMessage(String mailaddress, String about, CartSession cartSession) {
		final SpringTemplateEngine engine = new SpringTemplateEngine();
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setCharacterEncoding("UTF-8");
		engine.setTemplateResolver(templateResolver);
		
		//htmlへ情報を保存
		final Map<String, Object> variables = new HashMap<>();
		variables.put("h1_text", about);
		variables.put("products", cartSession.getCart());
		variables.put("sum", cartSession.getSum() - cartSession.getUsePoint());

		final Context context = new Context();
		context.setVariables(variables);
		
		final String htmlBody = engine.process("mail/html/htmlmail.html", context);

		try {
			final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
			// MimeMessageHelper 第2引数でマルチパート指定
			final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
			helper.setFrom("springhost415@gmail.com");
			helper.setTo(mailaddress);
			helper.setSubject(about);
			helper.setText(htmlBody, true);

			this.javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			
		}
	}
	
	//ユーザー登録完了メール
	public void SendRegisterMessage(String mailaddress, String about, String message) {
		final SpringTemplateEngine engine = new SpringTemplateEngine();
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setCharacterEncoding("UTF-8");
		engine.setTemplateResolver(templateResolver);

		final Map<String, Object> variables = new HashMap<>();
		variables.put("about", about);
		variables.put("message", message);

		final Context context = new Context();
		context.setVariables(variables);

		final String htmlBody = engine.process("mail/html/htmlregister.html", context);

		try {
			final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
			// MimeMessageHelper 第2引数でマルチパート指定
			final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
			helper.setFrom("springhost415@gmail.com");
			helper.setTo(mailaddress);
			helper.setSubject(about);
			helper.setText(htmlBody, true);

			this.javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {

		}
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
			SendRegisterMessage(findByUsername(username).getMailaddress(), "品川書店アカウント凍結", "あなたのアカウントが凍結されました。");
		}
	}
}