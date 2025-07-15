package com.example.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebShopController {

	@GetMapping("/login")
	public String loginForm() {
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.setAttribute("cart", null);
		return "redirect:/login";
	}
}
