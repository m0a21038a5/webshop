package com.example.service;

import org.springframework.stereotype.Service;

import com.example.repository.UserAlertRepository;

@Service
public class UserAlertService {
	
	private final UserAlertRepository userAlertRepository;
	
	public UserAlertService(UserAlertRepository userAlertRepository) {
		this.userAlertRepository = userAlertRepository;
	}
	
	//alert_review　に　通報者と通報されたユーザ　の組み合わせがあるか確認する
	public int Search_alert_count(String user_send_alert, String username) {
		return userAlertRepository.Search_alert_countRepo(user_send_alert, username);
	}

	public void ReviewAlertService(int user_alert_count, String username) {
		userAlertRepository.ReviewAlertRepo(user_alert_count, username);
	}
}
