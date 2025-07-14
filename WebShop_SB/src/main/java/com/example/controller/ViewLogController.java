package com.example.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.service.ViewLogService;

@Controller
public class ViewLogController {
	private final ViewLogService viewLogService;

	public ViewLogController(ViewLogService viewLogService) {
		this.viewLogService = viewLogService;
	}

	//閲覧履歴
	@GetMapping("/viewLog")
	public String ViewLog(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		List<com.example.model.ViewLog> list = viewLogService.findAllViewLog(userDetails.getUsername());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		for (com.example.model.ViewLog log : list) {
			log.setTime(log.getDate().format(formatter));
		}

		model.addAttribute("products", list);
		model.addAttribute("loginUser", SecurityContextHolder.getContext().getAuthentication().getName());
		return "viewlog";
	}
}
