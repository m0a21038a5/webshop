package com.example.repository;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.Inquiry;
import com.example.model.User;

@Repository
public class InquiryRepository {
	private JdbcTemplate jdbcTemplate;

	public InquiryRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	//問い合わせ
	public void insertInquiry(String subject, String content) {
		String query = "insert into inquiry (subject,content) values(?,?);";

		jdbcTemplate.update(query, subject, content);

	}

	//問い合わせ解答
	public void updateInquiryAnswerd(Inquiry inquiry) {
		String query = "update inquiry set answered = true, answercontent = ? where id = ?;";

		jdbcTemplate.update(query, inquiry.getAnswercontent(), inquiry.getId());

	}

	//解答済みの問い合わせ取得
	public List<Inquiry> selectAnsweredInquiry() {
		String query = "select * from inquiry where answered = 1;";
		List<Inquiry> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Inquiry.class));

		return list;

	}

	//未回答の問い合わせ取得
	public List<Inquiry> selectNotAnsweredInquiry() {

		String query = "select * from inquiry where answered = 0;";

		List<Inquiry> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Inquiry.class));

		return list;

	}

	//USERロールのユーザー名を全取得
	public List<User> findByAllUsers() {

		String query = "select username from users where role = \"USER\"; ";

		List<User> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(User.class));

		return list;

	}
}
