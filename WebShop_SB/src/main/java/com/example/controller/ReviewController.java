package com.example.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.service.CommentService;

@Controller
public class ReviewController {

	private final CommentService commentService;

	public ReviewController(CommentService commentService) {
		this.commentService = commentService;
	}

	//レビュー完了
	@PostMapping("/CompReview")
	public String Review(@RequestParam("id") int id, @RequestParam("writeReview") String writeReview,
			@AuthenticationPrincipal UserDetails userDetails, HttpSession session,Model model) {

		session.setAttribute("product_id", id);
		int product_id = (int) session.getAttribute("product_id");
		session.setAttribute("user_name", userDetails.getUsername());
		String user_name = (String) session.getAttribute("user_name");
		commentService.addReview(product_id, user_name, writeReview);
		model.addAttribute("product_id", product_id);
		return "CompReview";
	}
}
