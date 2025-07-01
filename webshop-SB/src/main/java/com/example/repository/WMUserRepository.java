package com.example.repository;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.Inquiry;
import com.example.model.User;

@Repository
public class WMUserRepository {

	private JdbcTemplate jdbcTemplate;

	public WMUserRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void updateInquiry() {

	}

	public void insertInquiry(String subject, String content) {
		String query = "insert into inquiry (subject,content) values(?,?);";

		jdbcTemplate.update(query, subject, content);

	}

	public void updateInquiryAnswerd(Inquiry inquiry) {
		String query = "update inquiry set answered = true, answercontent = ? where id = ?;";

		jdbcTemplate.update(query, inquiry.getAnswercontent(), inquiry.getId());

	}

	public List<Inquiry> selectAnsweredInquiry() {
		String query = "select * from inquiry where answered = 1;";
		List<Inquiry> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Inquiry.class));

		return list;

	}

	public List<Inquiry> selectNotAnsweredInquiry() {

		String query = "select * from inquiry where answered = 0;";

		List<Inquiry> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Inquiry.class));

		return list;

	}

	public List<User> findByAllUsers() {

		String query = "select username from users where role = \"USER\"; ";

		List<User> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(User.class));

		return list;

	}

}
