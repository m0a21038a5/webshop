package com.example.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.Comment;
import com.example.model.Product;
import com.example.model.SearchLog;
import com.example.service.CommentService;
import com.example.service.ProductService;
import com.example.service.ViewLogService;

@Controller
public class ProductController {
	private final ProductService productService;
	private final CommentService commentService;
	private final ViewLogService viewLogService;

	public ProductController(ProductService productService, CommentService commentService,
			ViewLogService viewLogService) {
		this.productService = productService;
		this.commentService = commentService;
		this.viewLogService = viewLogService;
	}

	//ホームページ
	@GetMapping("/homepage")
	public String homepage(Model model) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		// 検索履歴の取得（ログインユーザーが取得できた場合のみ）
		model.addAttribute("searchList", (username != null && !username.equals("anonymousUser"))
				? productService.findSearchTitleLog(username, "products")
				: null);

		//イチオシリスト
		model.addAttribute("recommend", productService.findRecommand());

		//ランキング
		List<Product> rankedProducts = productService.findSalesRank();
		for (int i = 0; i < rankedProducts.size(); i++) {
			rankedProducts.get(i).setRank(i + 1);
		}
		model.addAttribute("rank", rankedProducts);
		model.addAttribute("loginUser", username);
		return "homepage";
	}

	//作品一覧ページ
	@GetMapping("/products")
	public String listProducts(Model model, HttpSession session,
			@RequestParam(value = "rank", required = false) String sortrule,
			@RequestParam(value = "search", required = false) String search) {

		// ログインユーザー名の取得
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean isLoggedIn = username != null && !username.equals("anonymousUser");

		// 商品リストの取得
		List<Product> productList = productService.searchByKeyword(search);

		// 履歴保存
		if (isLoggedIn) {
			SearchLog searchLog = new SearchLog();
			searchLog.setUsername(username);
			searchLog.setPlace("products");
			searchLog.setTitle(search);
			productService.saveSearchLog(searchLog);
		}

		// 並び替え
		productService.sortProductList(productList, sortrule);

		// モデルへの追加
		model.addAttribute("products", productList);
		model.addAttribute("search", search);
		model.addAttribute("rank", sortrule);
		model.addAttribute("loginUser", username);
		model.addAttribute("searchList", isLoggedIn
				? productService.findSearchTitleLog(username, "products")
				: null);

		return "products";
	}

	//商品ページ
	@GetMapping("/products/{id}")
	public String showProductDetail(@PathVariable("id") int product_id, Model model) {
		Product product = productService.findById(product_id);

		if (product == null) {
			return "redirect:/homepage";
		}

		//コメント取得
		List<Comment> stringComment = commentService.findAllReviewId(product_id);

		// ログインユーザー名の取得
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean isLoggedIn = username != null && !username.equals("anonymousUser");

		//閲覧履歴保存
		if (isLoggedIn) {
			viewLogService.addViewLog(username, product_id);
		}

		if (stringComment == null) {
			model.addAttribute("TakeReview", "レビューはまだありません");
		}

		String sendAlertUser = username; //通報者　かつ　ログインユーザー

		String samplePage = "/images/" + product_id;
		for (int i = 1; i <= 3; i++) {
			if (i == 1) {
				model.addAttribute("SampleFileDirectory1", samplePage + "/Sample" + 1 + ".png");
			}
			if (i == 2) {
				model.addAttribute("SampleFileDirectory2", samplePage + "/Sample" + 2 + ".png");
			}
			if (i == 3) {
				model.addAttribute("SampleFileDirectory3", samplePage + "/Sample" + 3 + ".png");
			}
		}

		model.addAttribute("product", product);
		model.addAttribute("Review_user_name", sendAlertUser);
		model.addAttribute("TakeReview", commentService.TakeReview(product_id, sendAlertUser));
		model.addAttribute("loginUser", username);
		return "detail";
	}

	@GetMapping("/example")
	public String example(@RequestParam(name = "paramName") String paramName) {
		return "example";
	}

}
