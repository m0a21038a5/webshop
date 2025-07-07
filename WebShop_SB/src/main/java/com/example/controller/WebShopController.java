package com.example.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.Comment;
import com.example.model.Product;
import com.example.model.SearchLog;
import com.example.model.User;
import com.example.service.CommentService;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.example.service.WMProService;

@Controller
public class WebShopController {
	private final UserService userService;
	private final ProductService productService;
	private final CommentService commentService;

	private static final String MODEL_PATH = "model";
	
	public WebShopController(UserService userService, ProductService productService, CommentService commentService,
			WMProService wm)
			throws Exception {
		this.userService = userService;
		this.productService = productService;
		this.commentService = commentService;
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

	//音声処理
	@PostMapping(path = "/api/speech/recognize", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public ResponseEntity<String> recognizeSpeech(@RequestPart("audioFile") MultipartFile audioFile){
		return ResponseEntity.ok("夏目漱石");
	}

	@GetMapping("/products")
	public String listProducts(Model model, HttpSession session,
			@RequestParam(value = "rank", required = false) String sortrule,
			@RequestParam(value = "search", required = false) String search) {

		// ログインユーザー名の取得
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean isLoggedIn = username != null && !username.equals("anonymousUser");

		// 検索ワードの前処理（例: "検索" の除去）
		if (search != null) {
			search = search.replaceAll("検索", "").trim();
		}

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
		Comment stringComment = commentService.findAllReviewId(product_id);

		// ログインユーザー名の取得
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean isLoggedIn = username != null && !username.equals("anonymousUser");

		//閲覧履歴保存
		if (isLoggedIn) {
			productService.addViewLog(username, product_id);
		}

		if (stringComment == null) {
			model.addAttribute("TakeReview", "レビューはまだありません");
		} else {
			model.addAttribute("TakeReview", stringComment);
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
		model.addAttribute("TakeReview", productService.TakeReview(product_id, sendAlertUser));
		model.addAttribute("loginUser", username);
		return "detail";
	}

	//レビュー完了
	@PostMapping("/CompReview")
	public String Review(@RequestParam("id") int id, @RequestParam("writeReview") String writeReview,
			@AuthenticationPrincipal UserDetails userDetails, HttpSession session) {

		session.setAttribute("product_id", id);
		int product_id = (int) session.getAttribute("product_id");
		session.setAttribute("user_name", userDetails.getUsername());
		String user_name = (String) session.getAttribute("user_name");
		productService.SendReview(product_id, user_name, writeReview);

		return "CompReview";
	}
	
	//通報
	@PostMapping("/ReviewAlert")
	public String ReviewAlert(@AuthenticationPrincipal UserDetails userDetails,
			@RequestParam("id") int id,
			@RequestParam("revAle_username") String username,
			Model model, HttpSession session) {

		String user_send_alert = userDetails.getUsername(); //通報者
		User user = userService.findByUsername(username); //通報されたユーザ

		//alert_review　に　通報者と通報されたユーザ　の組み合わせがあるか確認する
		int real_count = userService.Search_alert_count(user_send_alert, username);

		//もし、上記で組み合わせがなかったら実行
		if (real_count == 0 && !(user_send_alert.equals(username))) {
			int user_alert_count = user.getAlert();
			user_alert_count = user_alert_count + 1;
			userService.ReviewAlertService(user_alert_count, username);
		}

		return "redirect:/products/" + id;
	}
	
	@GetMapping("/example")
	public String example(@RequestParam(name = "paramName") String paramName) {
		return "example";
	}

	@GetMapping("/login")
	public String loginForm() {
		userService.AddAdmin();
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.setAttribute("cart", null);
		return "redirect:/login";
	}
	
	//新規登録
	@GetMapping("/register")
	public String registerForm() {
		return "register"; // ユーザー登録フォームを表示
	}

	@PostMapping("/register")
	public String register(@RequestParam(name = "username") String username,
			@RequestParam(name = "password") String password, @RequestParam(name = "mailaddress") String mailaddress,
			@RequestParam(name = "address") String address, @RequestParam(name = "age") int age, Model model) {
		try {
			if (userService.userExists(username)) {
				model.addAttribute("error", "ユーザー名はすでに存在します。");
				return "register"; // エラーメッセージを表示
			}

			User newUser = new User();
			newUser.setUsername(username);
			newUser.setPassword(password);
			newUser.setMailaddress(mailaddress);
			newUser.setAddress(address);
			newUser.setAge(age);
			newUser.setRole("USER");
			newUser.setAlert(0);
			newUser.setEnabled(false);

			int result = userService.save(newUser); // ユーザーを保存
			if (result == 1) {
				userService.SendRegisterMessage(mailaddress, "品川書店ユーザー登録", "ユーザー登録が完了しました。");
				return "registrationComplete";
			} else {
				model.addAttribute("error", "登録できませんでした");
				return "register";
			}
		} catch (Exception e) {
			model.addAttribute("error", "エラーが発生しました: " + e.getMessage());
			return "register";
		}
	}
	
	//パスワード変更
	@GetMapping("/userUpdate")
	public String userView(HttpSession session, @AuthenticationPrincipal UserDetails userDetails, Model model) {
		model.addAttribute("username", userDetails.getUsername());
		model.addAttribute("loginUser", SecurityContextHolder.getContext().getAuthentication().getName());
		return "userForm";
	}

	@PostMapping("/userUpdate")
	public String userUpdate(@RequestParam("username") String username, @RequestParam("password") String password) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		userService.userUpdate(user);
		return "userUpdateComplete";
	}

	// 支店情報表示
	@GetMapping("/ShopLocation")
	public String ShopLocation(Model model) {
		model.addAttribute("ShopLocation", productService.shoplocationGet());
		return "/ShopLocation";
	}

	//現在位置取得
	@PostMapping("/ShopLocation")
	public String ShopLocation2(@RequestParam("latitude-output") String lat,
			@RequestParam("longitude-output") String lng,
			Model model, HttpSession session) {
		model.addAttribute("latitude-output", lat);
		model.addAttribute("longitude-output", lng);
		model.addAttribute("getnowlocation", productService.shoploSe(lat, lng));
		return "/ShopLocation";
	}
	
	//閲覧履歴
	@GetMapping("/viewLog")
	public String ViewLog(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		List<com.example.model.ViewLog> list = productService.findAllViewLog(userDetails.getUsername());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		for (com.example.model.ViewLog log : list) {
			log.setTime(log.getDate().format(formatter));
		}

		model.addAttribute("products", list);
		model.addAttribute("loginUser", SecurityContextHolder.getContext().getAuthentication().getName());
		return "viewlog";
	}
	
	//ユーザー情報更新
	@GetMapping("/settings")
	public String detailUpdate(Model model) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("username", username);
		model.addAttribute("address", userService.findByUsername(username).getAddress());
		model.addAttribute("age", userService.findByUsername(username).getAge());
		model.addAttribute("loginUser", username);
		return "userDetailUpdate";
	}

	@PostMapping("/settings")
	public String userChange(@RequestParam("username") String username, @RequestParam("address") String address,
			@RequestParam("age") int age) {
		User user = userService.findByUsername(username);
		user.setAddress(address);
		user.setAge(age);
		userService.updateuserAddress(user);
		userService.SendRegisterMessage(user.getMailaddress(), "品川書店アカウント情報変更", "あなたのアカウントが変更されました。");
		return "userUpdateComplete";
	}
	
	//メールアドレス更新
	@GetMapping("/email-settings")
	public String changeEmail(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		model.addAttribute("username", userDetails.getUsername());
		model.addAttribute("mailaddress", userService.findByUsername(userDetails.getUsername()).getMailaddress());
		model.addAttribute("loginUser", SecurityContextHolder.getContext().getAuthentication().getName());
		return "updateMailAddress";
	}

	@PostMapping("/email-settings")
	public String updateEmail(@RequestParam("username") String username,
			@RequestParam("mailaddress") String mailaddress) {
		User user = userService.findByUsername(username);
		user.setMailaddress(mailaddress);
		userService.updateuserAddress(user);
		userService.SendRegisterMessage(user.getMailaddress(), "品川書店アカウント情報変更", "あなたのアカウントが変更されました。");
		return "userUpdateComplete";
	}
}
