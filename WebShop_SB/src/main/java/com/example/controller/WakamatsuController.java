package com.example.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.BeforeProducts;
import com.example.model.BuyLog;
import com.example.model.Inquiry;
import com.example.model.Product;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.example.service.WMProService;
import com.example.service.WMUserService;

@Controller
public class WakamatsuController {

	private final UserService userService;
	private final ProductService productService;
	private final WMProService wm;
	private final WMUserService wmUser;

	public WakamatsuController(UserService userService, ProductService productService, WMProService wm,
			WMUserService wmUser) {
		this.userService = userService;
		this.productService = productService;
		this.wm = wm;
		this.wmUser = wmUser;
	}

	//購入履歴
	@GetMapping("/buylog")
	public String log(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		List<BuyLog> list = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		list = userService.findByLog(userDetails.getUsername());

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
		if (wm.findAllBeforeProduct().isEmpty()) {
			wm.backupPrice();
		} else {
			for (Product p : list) {
				wm.updateBackupPrice(p);
			}
		}
		wm.updateprice(discount, discountproduct);
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
		List<BeforeProducts> list = wm.findAllBeforeProduct();

		for (BeforeProducts bp : list) {
			wm.finishedPrice(bp.getPrice(), bp.getName());
		}

		return "redirect:/products";
	}

	//お問い合わせ
	@GetMapping("/inquiry")
	public String inquiry() {
		return "inquiry";
	}

	@PostMapping("/inquiry")
	public String inquiryform(@RequestParam("subject") String subject, @RequestParam("content") String content) {

		wmUser.insertInquiry(subject, content);

		return "redirect:/inquiry";

	}

	//Q&A
	@GetMapping("/qa")
	public String qa(Model model) {
		List<Inquiry> list = new ArrayList<>();
		list = wmUser.selectAnsweredInquiry();

		model.addAttribute("answeredinquiry", list);

		return "QA";
	}

	//お問い合わせの返答
	@GetMapping("/answer")
	public String answer(Model model) {
		List<Inquiry> list = new ArrayList<>();

		list = wmUser.selectNotAnsweredInquiry();

		model.addAttribute("inquiry", list);

		return "Answer";
	}

	@PostMapping("/answer")
	public String answerform(@RequestParam("answercontent") String answercontent,
			@RequestParam("inquiryid") int inquiryid) {
		List<Inquiry> list = new ArrayList<>();

		list = wmUser.selectNotAnsweredInquiry();
		for (Inquiry inquiry : list) {
			if (inquiry.getId() == inquiryid) {
				inquiry.setAnswercontent(answercontent);
				wmUser.updateInquiryAnswerd(inquiry);
			}
		}

		return "redirect:/answercomplete";

	}

	//お問い合わせ返答完了ページ
	@GetMapping("/answercomplete")
	public String answercomplete() {
		return "AnswerComplete";
	}

}