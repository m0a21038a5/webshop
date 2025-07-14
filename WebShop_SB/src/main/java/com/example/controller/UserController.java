package com.example.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.User;
import com.example.service.MailService;
import com.example.service.UserService;

@Controller
public class UserController {
	private final UserService userService;
	private final MailService mailService;

	public UserController(UserService userService,MailService mailService) {
		this.userService = userService;
		this.mailService = mailService;
	}

	//新規登録
	@GetMapping("/register")
	public String registerForm() {
		return "register"; // ユーザー登録フォームを表示
	}

	@PostMapping("/register")
	public String register(@RequestParam(name = "username") String username,
			@RequestParam(name = "password") String password, @RequestParam(name = "mailaddress") String mailaddress,
			@RequestParam(name = "address") String address, @RequestParam(name = "age") int age, Model model) {
		try {
			if (userService.userExists(username)) {
				model.addAttribute("error", "ユーザー名はすでに存在します。");
				return "register"; // エラーメッセージを表示
			}

			User newUser = new User();
			newUser.setUsername(username);
			newUser.setPassword(password);
			newUser.setMailaddress(mailaddress);
			newUser.setAddress(address);
			newUser.setAge(age);
			newUser.setRole("USER");
			newUser.setAlert(0);
			newUser.setEnabled(false);

			int result = userService.save(newUser); // ユーザーを保存
			if (result == 1) {
				mailService.setRegisterMessage(mailaddress, "品川書店ユーザー登録", "ユーザー登録が完了しました。");
				return "registrationComplete";
			} else {
				model.addAttribute("error", "登録できませんでした");
				return "register";
			}
		} catch (Exception e) {
			model.addAttribute("error", "エラーが発生しました: " + e.getMessage());
			return "register";
		}
	}

	//パスワード変更
	@GetMapping("/userUpdate")
	public String userView(HttpSession session, @AuthenticationPrincipal UserDetails userDetails, Model model) {
		model.addAttribute("username", userDetails.getUsername());
		model.addAttribute("loginUser", SecurityContextHolder.getContext().getAuthentication().getName());
		return "userForm";
	}

	@PostMapping("/userUpdate")
	public String userUpdate(@RequestParam("username") String username, @RequestParam("password") String password) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		userService.userUpdate(user);
		return "userUpdateComplete";
	}

	//ユーザー情報更新
	@GetMapping("/settings")
	public String detailUpdate(Model model) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("username", username);
		model.addAttribute("address", userService.findByUsername(username).getAddress());
		model.addAttribute("age", userService.findByUsername(username).getAge());
		model.addAttribute("loginUser", username);
		return "userDetailUpdate";
	}

	@PostMapping("/settings")
	public String userChange(@RequestParam("username") String username, @RequestParam("address") String address,
			@RequestParam("age") int age) {
		User user = userService.findByUsername(username);
		user.setAddress(address);
		user.setAge(age);
		userService.updateuserAddress(user);
		mailService.setRegisterMessage(user.getMailaddress(), "品川書店アカウント情報変更", "あなたのアカウントが変更されました。");
		return "userUpdateComplete";
	}

	//メールアドレス更新
	@GetMapping("/email-settings")
	public String changeEmail(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		model.addAttribute("username", userDetails.getUsername());
		model.addAttribute("mailaddress", userService.findByUsername(userDetails.getUsername()).getMailaddress());
		model.addAttribute("loginUser", SecurityContextHolder.getContext().getAuthentication().getName());
		return "updateMailAddress";
	}

	@PostMapping("/email-settings")
	public String updateEmail(@RequestParam("username") String username,
			@RequestParam("mailaddress") String mailaddress) {
		User user = userService.findByUsername(username);
		user.setMailaddress(mailaddress);
		userService.updateuserAddress(user);
		mailService.setRegisterMessage(user.getMailaddress(), "品川書店アカウント情報変更", "あなたのアカウントが変更されました。");
		return "userUpdateComplete";
	}
}
