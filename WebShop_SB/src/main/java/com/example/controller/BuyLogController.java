package com.example.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.model.BuyLog;
import com.example.service.BuyLogService;
import com.example.service.ProductService;

@Controller
public class BuyLogController {

	private final BuyLogService buyLogService;
	private final ProductService productService;

	public BuyLogController(BuyLogService buyLogService, ProductService productService) {
		this.buyLogService = buyLogService;
		this.productService = productService;
	}

	//購入履歴
	@GetMapping("/buylog")
	public String log(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		List<BuyLog> list = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		list = buyLogService.findByLog(userDetails.getUsername());

		for (BuyLog log : list) {
			if (productService.findById(log.getProductid()) == null) {
				log.setProductname("その商品は削除されました");
			} else {
				String productname = productService.findById(log.getProductid()).getTitle();
				log.setProductname(productname);
				log.setTime(log.getCreated_at().format(formatter));
			}
		}
		model.addAttribute("buylog", list);

		return "BuyLog";
	}
}
