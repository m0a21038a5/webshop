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
import com.example.model.User;
import com.example.service.ProductService;
import com.example.service.ReadingService;
import com.example.service.UserService;
import com.example.session.StockSession;
import com.example.session.StockSessionManager;

@Controller
public class AdminController {

	private final UserService userService;
	private final ProductService productService;
	private final StockSessionManager stockSessionManager;
	private final ReadingService readingService;

	public AdminController(UserService userService, ProductService productService,
			StockSessionManager stockSessionManager, ReadingService readingService) {
		this.userService = userService;
		this.productService = productService;
		this.stockSessionManager = stockSessionManager;
		this.readingService = readingService;
	}

	// 管理者ページ
	@GetMapping("/admin")
	public String admin(Model model) {
		return "admin";
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

		model.addAttribute("genrelist", productService.findAllGenre());

		//検索履歴
		model.addAttribute("idList", productService.findSearchIdLog(userDetails.getUsername(), "stock"));
		model.addAttribute("titleList", productService.findSearchTitleLog(userDetails.getUsername(), "stock"));
		model.addAttribute("authorList", productService.findSearchAuthorLog(userDetails.getUsername(), "stock"));
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
			@RequestParam(value = "release_day",required = false) String release_day,
			@RequestParam("price") int price, @RequestParam("stock") int stock, @RequestParam("genre") String genre,
			@RequestParam("imgLink") String imgLink, @RequestParam("notice") String notice,
			@RequestParam("sales") int sales, Model model, HttpSession session) {

		Product product = productService.findById(id);
		product.setTitle(title);
		product.setAuthor(author);
		product.setRelease_day(release_day);
		product.setStock(stock);
		product.setPrice(price);
		product.setStock(stock);
		product.setSales(sales);
		product.setImgLink(imgLink);
		product.setGenre(genre);
		product.setNotice(notice);

		//商品情報更新
		productService.updateProduct(product);

		//読み仮名追加
		readingService.updateReading(id, title_hira, title_kana, title_romaji, author_hira, author_kana, author_romaji);

		StockSession stockSession = stockSessionManager.getStockSession(session);
		List<Product> list = stockSession.getStock();

		for (Product p : list) {
			if (p.getId() == id) {
				p.setTitle(title);
				p.setAuthor(author);
				p.setRelease_day(release_day);
				p.setStock(stock);
				p.setPrice(price);
				p.setStock(stock);
				p.setSales(sales);
				p.setImgLink(imgLink);
				p.setGenre(genre);
				p.setNotice(notice);
				break;
			}
		}

		stockSession.setStock(list);

		return "redirect:/Stock";
	}

	// 商品追加
	@PostMapping("/addProductDefault")
	public String addSyouhin(HttpSession session) {

		Product product = new Product();
		product.setTitle("タイトル");
		product.setAuthor("著者");
		product.setRelease_day("発売日");
		product.setStock(0);
		product.setQuantity(0);
		product.setPrice(0);
		product.setSales(0);
		product.setImgLink("画像リンク");
		product.setGenre("ジャンル");
		product.setRecommand(false);
		product.setNotice("説明");
		product.setView(false);
		productService.insert(product);

		StockSession stockSession = stockSessionManager.getStockSession(session);
		stockSession.setStock(productService.findAll());
		stockSessionManager.clearSearchConditions(session);
		return "redirect:/Stock";
	}

	// 商品削除
	@PostMapping("/removeProduct")
	public String delete(@RequestParam("id") int id, HttpSession session) {
		productService.delete(id);
		StockSession stockSession = stockSessionManager.getStockSession(session);
		stockSession.setStock(productService.findAll());
		return "redirect:/Stock";
	}

	// ジャンル追加
	@PostMapping("/addGenre")
	public String addGenre(@RequestParam("id") int id, @RequestParam("NewGenre") String genre) {
		productService.addNewGenre(genre, id);
		return "redirect:/Stock";
	}

	// ジャンル削除
	@PostMapping("/deleteGenre")
	public String deleteGenre(@RequestParam("id") int id, @RequestParam("genre") String genre) {
		productService.deleteGenre(genre, id);
		return "redirect:/Stock";
	}

	// イチオシ機能
	@PostMapping("/ChangeRecommand")
	public String Recommend(Model model, @RequestParam(value = "recommand", required = false) List<String> recommand,
			@RequestParam("id") int id, HttpSession session) {

		boolean flag = false;
		if (recommand != null) {
			flag = true;
		}

		if (flag && !productService.CountRecommand()) {
			flag = false;
		}

		Product product = productService.findById(id);
		product.setRecommand(flag);

		StockSession stockSession = stockSessionManager.getStockSession(session);

		List<Product> list = stockSession.getStock();
		productService.updateProduct(product);

		for (Product p : list) {
			if (p.getId() == id) {
				p.setRecommand(flag);
				break;
			}
		}

		stockSession.setStock(list);

		return "redirect:/Stock";
	}

	//公開・非公開
	@PostMapping("/ChangeView")
	public String View(Model model, @RequestParam(value = "view", required = false) List<String> view,
			@RequestParam("id") int id, HttpSession session) {

		boolean flag = false;
		if (view != null) {
			flag = true;
		}

		Product product = productService.findById(id);
		product.setView(flag);

		StockSession stockSession = stockSessionManager.getStockSession(session);

		List<Product> list = stockSession.getStock();
		productService.updateProduct(product);

		for (Product p : list) {
			if (p.getId() == id) {
				p.setView(flag);
				break;
			}
		}

		stockSession.setStock(list);

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
		List<Product> result = productService.findSearchAll(num, title, author, genre);
		if (result == null) {
			result = productService.findAll();
			stockSessionManager.clearSearchConditions(session);
		}
		stockSession.setStock(result);

		return "redirect:/Stock";

	}

	// アカウント凍結・解除
	@PostMapping("/BanUser")
	public String banUser(@RequestParam("username") String username,
			@RequestParam(value = "enabled", required = false) List<String> enabled) {
		User user = userService.findByUsername(username);

		boolean flag = false;

		if (enabled != null) {
			flag = true;
		}

		if (flag) {
			userService.SendRegisterMessage(user.getMailaddress(), "品川書店アカウント凍結", "あなたのアカウントが凍結されました。");
		} else {
			userService.SendRegisterMessage(user.getMailaddress(), "品川書店アカウント凍結", "あなたのアカウント凍結が解除されました。");
		}

		user.setEnabled(flag);

		userService.updateEnabled(user);
		return "redirect:/UserInfo";
	}

	// ユーザー情報表示
	@GetMapping("/UserInfo")
	public String userInfo(Model model) {
		model.addAttribute("User", userService.findAllUser());
		return "deleteUser";
	}

	// ユーザーロール変更
	@PostMapping("/UserInfo")
	public String ChangeUser(@RequestParam("username") String username, @RequestParam("role") String role) {
		User user = userService.findByUsername(username);
		user.setRole(role);

		userService.SendRegisterMessage(user.getMailaddress(), "品川書店アカウント変更", "あなたのアカウントが" + role + "になりました。");

		userService.updateRole(user);
		return "redirect:/UserInfo";
	}

}
