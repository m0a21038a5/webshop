package com.example.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.User;
import com.example.service.MailService;
import com.example.service.ProductService;
import com.example.service.ReadingService;
import com.example.service.UserService;
import com.example.session.StockSessionManager;

@Controller
public class AdminController {

	private final UserService userService;
	private final ProductService productService;
	private final StockSessionManager stockSessionManager;
	private final ReadingService readingService;
	private final MailService mailService;

	public AdminController(UserService userService, ProductService productService,
			StockSessionManager stockSessionManager, ReadingService readingService,MailService mailService) {
		this.userService = userService;
		this.productService = productService;
		this.stockSessionManager = stockSessionManager;
		this.readingService = readingService;
		this.mailService = mailService;
	}

	// 管理者ページ
	@GetMapping("/admin")
	public String admin(Model model) {
		return "admin";
	}

	
	// アカウント凍結・解除
	@PostMapping("/BanUser")
	public String banUser(@RequestParam("username") String username,
			@RequestParam(value = "enabled", required = false) List<String> enabled) {
		User user = userService.findByUsername(username);

		boolean flag = false;

		if (enabled != null) {
			flag = true;
		}

		if (flag) {
			mailService.setRegisterMessage(user.getMailaddress(), "品川書店アカウント凍結", "あなたのアカウントが凍結されました。");
		} else {
			mailService.setRegisterMessage(user.getMailaddress(), "品川書店アカウント凍結", "あなたのアカウント凍結が解除されました。");
		}

		user.setEnabled(flag);

		userService.updateEnabled(user);
		return "redirect:/UserInfo";
	}

	// ユーザー情報表示
	@GetMapping("/UserInfo")
	public String userInfo(Model model) {
		model.addAttribute("User", userService.findAllUser());
		return "deleteUser";
	}

	// ユーザーロール変更
	@PostMapping("/UserInfo")
	public String ChangeUser(@RequestParam("username") String username, @RequestParam("role") String role) {
		User user = userService.findByUsername(username);
		user.setRole(role);

		mailService.setRegisterMessage(user.getMailaddress(), "品川書店アカウント変更", "あなたのアカウントが" + role + "になりました。");

		userService.updateRole(user);
		return "redirect:/UserInfo";
	}

}
