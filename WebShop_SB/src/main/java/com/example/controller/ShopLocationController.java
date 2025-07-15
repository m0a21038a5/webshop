package com.example.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.service.ShopLocationService;

@Controller
public class ShopLocationController {
	
	private final ShopLocationService shopLocationService;
	
	public ShopLocationController(ShopLocationService shopLocationService) {
		this.shopLocationService = shopLocationService;
	}
	
	// 支店情報表示
	@GetMapping("/ShopLocation")
	public String ShopLocation(Model model) {
		model.addAttribute("ShopLocation", shopLocationService.shoplocationGet());
		return "/ShopLocation";
	}

	//現在位置取得
	@PostMapping("/ShopLocation")
	public String ShopLocation2(@RequestParam("latitude-output") String lat,
			@RequestParam("longitude-output") String lng,
			Model model, HttpSession session) {
		model.addAttribute("latitude-output", lat);
		model.addAttribute("longitude-output", lng);
		model.addAttribute("getnowlocation", shopLocationService.shoploSe(lat, lng));
		return "/ShopLocation";
	}

}
