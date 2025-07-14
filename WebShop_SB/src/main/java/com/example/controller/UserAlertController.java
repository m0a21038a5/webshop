package com.example.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.User;
import com.example.service.UserAlertService;
import com.example.service.UserService;

@Controller
public class UserAlertController {
	private final UserService userService;
	private final UserAlertService userAlertService;
	
	public UserAlertController(UserService userService,UserAlertService userAlertService) {
		this.userService = userService;
		this.userAlertService = userAlertService;
	}
	
	//通報
	@PostMapping("/ReviewAlert")
	public String ReviewAlert(@AuthenticationPrincipal UserDetails userDetails,
			@RequestParam("id") int id,
			@RequestParam("revAle_username") String username,
			Model model, HttpSession session) {

		String user_send_alert = userDetails.getUsername(); //通報者
		User user = userService.findByUsername(username); //通報されたユーザ

		//alert_review　に　通報者と通報されたユーザ　の組み合わせがあるか確認する
		int real_count = userAlertService.Search_alert_count(user_send_alert, username);

		//もし、上記で組み合わせがなかったら実行
		if (real_count == 0 && !(user_send_alert.equals(username))) {
			int user_alert_count = user.getAlert();
			user_alert_count = user_alert_count + 1;
			userAlertService.ReviewAlertService(user_alert_count, username);
		}

		return "redirect:/products/" + id;
	}
	
	
}
