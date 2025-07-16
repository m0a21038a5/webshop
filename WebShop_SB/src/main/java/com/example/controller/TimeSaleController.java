package com.example.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.BeforeProducts;
import com.example.model.Product;
import com.example.service.ProductService;
import com.example.service.TimeSaleService;

@Controller
public class TimeSaleController {

	private final TimeSaleService timeSaleService;
	private final ProductService productService;

	public TimeSaleController(TimeSaleService timeSaleService, ProductService productService) {
		this.timeSaleService = timeSaleService;
		this.productService = productService;
	}

	//タイムセール
	@GetMapping("/timesale")
	public String timesale(HttpSession session, Model model) {
		List<Product> list = productService.findAll();
		session.setAttribute("timesaleproduct", list);

		if (session.getAttribute("end") == null) {
			session.setAttribute("end", null);
		}
		return "timesale";
	}

	@PostMapping("/timesale")
	public String timesaleform(@RequestParam("end") LocalDateTime end, @RequestParam("discount") int discount,
			@RequestParam("discountproduct") String discountproduct, Model model, HttpSession session) {

		List<Product> list = productService.findAll();
		if (timeSaleService.findAllBeforeProduct().isEmpty()) {
			timeSaleService.backupPrice();
		} else {
			for (Product p : list) {
				timeSaleService.updateBackupPrice(p);
			}
		}
		timeSaleService.updateprice(discount, discountproduct);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH mm ss");
		String endTime = end.format(formatter);
		String[] splitTime = endTime.split("\\s+");

		session.setAttribute("end", splitTime);
		session.setAttribute("discountproduct", discountproduct);
		return "redirect:/products";

	}

	//タイムセール終了時に実行
	@PostMapping("/timesalefinish")
	public String timesalefinish() {
		List<BeforeProducts> list = timeSaleService.findAllBeforeProduct();

		for (BeforeProducts bp : list) {
			timeSaleService.finishedPrice(bp.getPrice(), bp.getName());
		}

		return "redirect:/products";
	}

}
