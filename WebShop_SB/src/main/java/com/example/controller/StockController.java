package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.Product;
import com.example.model.Readings;
import com.example.service.ProductService;
import com.example.service.ReadingService;
import com.example.service.SearchLogService;
import com.example.service.SearchService;
import com.example.service.StockService;
import com.example.session.StockSession;
import com.example.session.StockSessionManager;

@Controller
public class StockController {
	private final ProductService productService;
	private final StockService stockService;
	private final StockSessionManager stockSessionManager;
	private final ReadingService readingService;
	private final SearchLogService searchLogService;
	private final SearchService searchService;

	public StockController(StockService stockService, ProductService productService,
			StockSessionManager stockSessionManager, ReadingService readingService, SearchLogService searchLogService,
			SearchService searchService) {
		this.productService = productService;
		this.stockService = stockService;
		this.stockSessionManager = stockSessionManager;
		this.readingService = readingService;
		this.searchLogService = searchLogService;
		this.searchService = searchService;
	}

	// 在庫管理
	@GetMapping("/Stock")
	public String StockManager(Model model, HttpSession session, @AuthenticationPrincipal UserDetails userDetails) {

		StockSession stockSession = stockSessionManager.getStockSession(session);
		List<Product> products = new ArrayList<>();
		if (stockSession.getStock() == null) {
			products = productService.findAll();
			stockSession.setStock(products);
		} else {
			products = stockSession.getStock();
		}

		for (Product product : products) {
			Readings readings = readingService.findByProductId(product.getId());
			product.setReadings(readings);
		}

		model.addAttribute("list", products);
		//検索ログ保存
		stockSessionManager.transferSearchConditionsToModel(session, model);

		model.addAttribute("genrelist", stockService.findAllGenre());

		//検索履歴
		model.addAttribute("idList", searchLogService.findSearchIdLog(userDetails.getUsername(), "stock"));
		model.addAttribute("titleList", searchLogService.findSearchTitleLog(userDetails.getUsername(), "stock"));
		model.addAttribute("authorList", searchLogService.findSearchAuthorLog(userDetails.getUsername(), "stock"));
		return "Stock";
	}

	// 在庫情報アップデート
	@PostMapping("/addProduct")
	public String addProduct(@RequestParam("id") int id,
			@RequestParam("title") String title,
			@RequestParam("title_hira") String title_hira,
			@RequestParam("title_kana") String title_kana,
			@RequestParam("title_romaji") String title_romaji,
			@RequestParam("author") String author,
			@RequestParam("author_hira") String author_hira,
			@RequestParam("author_kana") String author_kana,
			@RequestParam("author_romaji") String author_romaji,
			@RequestParam(value = "release_day", required = false) String release_day,
			@RequestParam("price") int price, @RequestParam("stock") int stock, @RequestParam("genre") String genre,
			@RequestParam("imgLink") String imgLink, @RequestParam("notice") String notice,
			@RequestParam("sales") int sales, Model model, HttpSession session) {

		Product product = stockService.setProduct(id, title, author, release_day, price, stock, sales, imgLink, genre,
				notice);

		//商品情報更新
		stockService.updateProduct(product);

		//読み仮名追加
		readingService.updateReading(id, title_hira, title_kana, title_romaji, author_hira, author_kana, author_romaji);

		stockSessionManager.updateProductList(session, product);

		return "redirect:/Stock";
	}

	// 商品追加
	@PostMapping("/addProductDefault")
	public String addSyouhin(HttpSession session) {

		stockService.setDefaultProduct();

		StockSession stockSession = stockSessionManager.getStockSession(session);
		stockSession.setStock(productService.findAll());
		stockSessionManager.clearSearchConditions(session);
		return "redirect:/Stock";
	}

	// 商品削除
	@PostMapping("/removeProduct")
	public String delete(@RequestParam("id") int id, HttpSession session) {
		stockService.delete(id);
		StockSession stockSession = stockSessionManager.getStockSession(session);
		stockSession.setStock(productService.findAll());
		return "redirect:/Stock";
	}

	// ジャンル追加
	@PostMapping("/addGenre")
	public String addGenre(@RequestParam("id") int id, @RequestParam("NewGenre") String genre) {
		stockService.addNewGenre(genre, id);
		return "redirect:/Stock";
	}

	// ジャンル削除
	@PostMapping("/deleteGenre")
	public String deleteGenre(@RequestParam("id") int id, @RequestParam("genre") String genre) {
		stockService.deleteGenre(genre, id);
		return "redirect:/Stock";
	}

	// イチオシ機能
	@PostMapping("/ChangeRecommand")
	public String Recommend(Model model, @RequestParam(value = "recommand", required = false) List<String> recommand,
			@RequestParam("id") int id, HttpSession session) {

		stockService.setRecommand(session, recommand, id);

		return "redirect:/Stock";
	}

	//公開・非公開
	@PostMapping("/ChangeView")
	public String View(Model model, @RequestParam(value = "view", required = false) List<String> view,
			@RequestParam("id") int id, HttpSession session) {

		stockService.setView(session, view, id);

		return "redirect:/Stock";
	}

	//検索
	@PostMapping("/searchStock")
	public String searchStock(@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "author", required = false) String author,
			@RequestParam(value = "genre", required = false) String genre, Model model, HttpSession session) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		StockSession stockSession = stockSessionManager.getStockSession(session);

		// 🔸 検索条件をセッションに保存 & ログ登録
		int num = stockSessionManager.updateSearchConditions(stockSession, username, id, title, author, genre);

		// 🔸 検索結果をセッションに保存
		List<Product> result = searchService.findSearchAll(num, title, author, genre);
		if (result == null) {
			result = productService.findAll();
			stockSessionManager.clearSearchConditions(session);
		}
		stockSession.setStock(result);

		return "redirect:/Stock";

	}

}
