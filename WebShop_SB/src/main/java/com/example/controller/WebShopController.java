package com.example.controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.service.CartService;
import com.example.service.CommentService;
import com.example.service.ProductService;
import com.example.service.RecommendationService;
import com.example.service.UserService;
import com.example.service.WMProService;

@Controller
public class WebShopController {
	private WMProService wm;
	private final UserService userService;
	private final ProductService productService;
	private final CommentService commentService;
	//private final SpeechClient speechClient;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private RecommendationService recommendationService;
	
	public WebShopController(UserService userService, ProductService productService, CommentService commentService,
			WMProService wm)
			throws Exception {
		this.userService = userService;
		this.productService = productService;
		this.commentService = commentService;
		this.wm = wm;

		// SpeechClientの初期化
//		String quotaProjectId = "gp-08-osusi"; // プロジェクトIDを指定
//		SpeechSettings speechSettings = SpeechSettings.newBuilder()
//				.setHeaderProvider(FixedHeaderProvider.create("x-goog-user-project", quotaProjectId)).build();
//		this.speechClient = SpeechClient.create(speechSettings);
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
		model.addAttribute("loginUser",username);
		return "homepage";
	}

	@PostMapping(path = "/api/speech/recognize", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public ResponseEntity<String> recognizeSpeech(@RequestPart("audioFile") MultipartFile audioFile)
			throws IOException {
		//String transcript = transcribe(audioFile.getBytes());
		return ResponseEntity.ok("夏目漱石");
	}

//	private String transcribe(byte[] audioBytes) throws IOException {
//		try {
//			RecognitionConfig config = RecognitionConfig.newBuilder().setEncoding(AudioEncoding.LINEAR16)
//					.setLanguageCode("ja-JP").setSampleRateHertz(11025).build();
//			RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(ByteString.copyFrom(audioBytes)).build();
//
//			RecognizeResponse response = speechClient.recognize(config, audio);
//			return response.getResultsList().stream().flatMap(r -> r.getAlternativesList().stream())
//					.map(SpeechRecognitionAlternative::getTranscript).collect(Collectors.joining(" "));
//		} catch (ApiException e) {
//			return "エラーが発生しました: " + e.getMessage();
//		}
//	}

//	@PreDestroy
//	public void shutdown() {
//		if (speechClient != null) {
//			speechClient.shutdown();
//		}
//	}

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
	    productService.sortProductList(productList,sortrule);

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
		

		
		String sendAlertUser = username;		//通報者　かつ　ログインユーザー
			
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
		model.addAttribute("TakeReview", productService.TakeReview(product_id,  sendAlertUser));
		model.addAttribute("loginUser",username);
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

	@PostMapping("/ReviewAlert")
	public String ReviewAlert(@AuthenticationPrincipal UserDetails userDetails,
								@RequestParam("id") int id,
								@RequestParam("revAle_username") String username,
								Model model, HttpSession session) {
		
		String user_send_alert = userDetails.getUsername(); 	//通報者
		User user = userService.findByUsername(username);		//通報されたユーザ
		
		//alert_review　に　通報者と通報されたユーザ　の組み合わせがあるか確認する
		int real_count = userService.Search_alert_count(user_send_alert, username);
		
		//もし、上記で組み合わせがなかったら実行
		if(real_count==0 && !(user_send_alert.equals(username))) {
			int user_alert_count = user.getAlert();
			user_alert_count = user_alert_count+1;
			userService.ReviewAlertService(user_alert_count, username);
		}
		
		return "redirect:/products/"+id;
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

	

	@GetMapping("/userUpdate")
	public String userView(HttpSession session, @AuthenticationPrincipal UserDetails userDetails, Model model) {
		model.addAttribute("username", userDetails.getUsername());
		model.addAttribute("loginUser",SecurityContextHolder.getContext().getAuthentication().getName());
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

	// 管理者ページ
	@GetMapping("/admin")
	public String admin(Model model) {
		return "admin";
	}

	// 在庫管理
	@GetMapping("/Stock")
	public String StockManager(Model model, HttpSession session, @AuthenticationPrincipal UserDetails userDetails) {

		//検索ログ保存
		if (session.getAttribute("stock") == null) {
			model.addAttribute("list", productService.findAll());
			session.setAttribute("stock", productService.findAll());
		} else {
			model.addAttribute("list", session.getAttribute("stock"));
		}

		if (session.getAttribute("searchId") != null) {
			model.addAttribute("searchId", session.getAttribute("searchId"));
		} else {
			model.addAttribute("searchId", null);
		}

		if (session.getAttribute("searchTitle") != null) {
			model.addAttribute("searchTitle", session.getAttribute("searchTitle"));
		} else {
			model.addAttribute("searchTitle", null);
		}

		if (session.getAttribute("searchAuthor") != null) {
			model.addAttribute("searchAuthor", session.getAttribute("searchAuthor"));
		} else {
			model.addAttribute("searchAuthor", null);
		}

		if (session.getAttribute("searchGenre") != null) {
			model.addAttribute("searchGenre", session.getAttribute("searchGenre"));
		} else {
			model.addAttribute("searchGenre", null);
		}

		model.addAttribute("genrelist", productService.findAllGenre());

		//検索履歴
		model.addAttribute("idList", productService.findSearchIdLog(userDetails.getUsername(), "stock"));
		model.addAttribute("titleList", productService.findSearchTitleLog(userDetails.getUsername(), "stock"));
		model.addAttribute("authorList", productService.findSearchAuthorLog(userDetails.getUsername(), "stock"));
		return "Stock";
	}

	// 在庫情報アップデート
	@PostMapping("/addProduct")
	public String addProduct(@RequestParam("id") int id, @RequestParam("title") String title,
			@RequestParam("author") String author, @RequestParam("release_day") String release_day,
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

		productService.updateProduct(product);

		List<Product> list = (List<Product>) session.getAttribute("stock");

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

		session.setAttribute("stock", list);

		return "redirect:/Stock";
	}

	// 商品追加
	@PostMapping("/addsyouhin")
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

		session.setAttribute("stock", productService.findAll());
		return "redirect:/Stock";
	}

	// 商品削除
	@PostMapping("/delete2")
	public String delete(@RequestParam("id") int id, HttpSession session) {
		productService.delete(id);
		session.setAttribute("stock", productService.findAll());
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

	// 一押し機能
	@PostMapping("/ChangeRecommand")
	public String Recommend(Model model, @RequestParam(value = "recommand", required = false) List<String> recommand,
			@RequestParam("id") int id, HttpSession session) {

		boolean flag = false;
		if (recommand != null) {
			flag = true;
			// System.out.println(productService.CountRecommend());
		}

		if (flag && !productService.CountRecommand()) {
			flag = false;
		}

		Product product = productService.findById(id);
		product.setRecommand(flag);

		List<Product> list = (List<Product>) session.getAttribute("stock");
		productService.updateProduct(product);

		for (Product p : list) {
			if (p.getId() == id) {
				p.setRecommand(flag);
				break;
			}
		}

		session.setAttribute("stock", list);

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

		List<Product> list = (List<Product>) session.getAttribute("stock");
		productService.updateProduct(product);

		for (Product p : list) {
			if (p.getId() == id) {
				p.setView(flag);
				break;
			}
		}
		session.setAttribute("stock", list);

		return "redirect:/Stock";
	}
 
	//検索
	@PostMapping("/searchStock")
	public String searchStock(@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "author", required = false) String author,
			@RequestParam(value = "genre", required = false) String genre, Model model, HttpSession session) {

		session.setAttribute("genre", genre);

		model.addAttribute("genrelist", productService.findAllGenre());
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		int num = 0;
		if (!id.isBlank() && !id.isEmpty()) {
			num = Integer.parseInt(id);
			session.setAttribute("searchId", id);
			SearchLog searchlog = new SearchLog();
			searchlog.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			searchlog.setPlace("stock");
			searchlog.setId(num);
			productService.saveSearchLog(searchlog);
		} else {
			session.setAttribute("searchId", null);
		}

		if (!title.isBlank() && !title.isEmpty()) {
			session.setAttribute("searchTitle", title);
			SearchLog searchlog = new SearchLog();
			searchlog.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			searchlog.setPlace("stock");
			searchlog.setTitle(title);
			productService.saveSearchLog(searchlog);
		} else {
			session.setAttribute("searchTitle", null);
		}

		if (!author.isBlank() && !author.isEmpty()) {
			session.setAttribute("searchAuthor", author);
			SearchLog searchlog = new SearchLog();
			searchlog.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			searchlog.setPlace("stock");
			searchlog.setAuthor(author);
			productService.saveSearchLog(searchlog);
		} else {
			session.setAttribute("searchAuthor", null);
		}

		if (!genre.isBlank() && !genre.isEmpty()) {
			session.setAttribute("searchGenre", genre);
		} else {
			session.setAttribute("searchGenre", null);
		}

		if (productService.findSearchAll(num, title, author, genre) == null) {
			session.setAttribute("stock", productService.findAll());
		}

		session.setAttribute("stock", productService.findSearchAll(num, title, author, genre));

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

	//////ここから　0615中村追加　位置情報機能
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
	//////ここまで　0615中村追加　位置情報機能

	@GetMapping("/viewLog")
	public String ViewLog(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		List<com.example.model.ViewLog> list = productService.findAllViewLog(userDetails.getUsername());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		for (com.example.model.ViewLog log : list) {
			log.setTime(log.getDate().format(formatter));
		}

		model.addAttribute("products", list);
		model.addAttribute("loginUser",SecurityContextHolder.getContext().getAuthentication().getName());
		return "viewlog";
	}
	
	@GetMapping("/settings")
	public String detailUpdate(Model model) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("username",username);
		model.addAttribute("address", userService.findByUsername(username).getAddress());
		model.addAttribute("age", userService.findByUsername(username).getAge());
		model.addAttribute("loginUser",SecurityContextHolder.getContext().getAuthentication().getName());
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
	
	@GetMapping("/email-settings")
	public String changeEmail(Model model,@AuthenticationPrincipal UserDetails userDetails) {
		model.addAttribute("username",userDetails.getUsername());
		model.addAttribute("mailaddress", userService.findByUsername(userDetails.getUsername()).getMailaddress());
		model.addAttribute("loginUser",SecurityContextHolder.getContext().getAuthentication().getName());
		return "updateMailAddress";
	}
	
	@PostMapping("/email-settings")
	public String updateEmail(@RequestParam("username") String username,@RequestParam("mailaddress") String mailaddress) {
		User user = userService.findByUsername(username);
		user.setMailaddress(mailaddress);
		userService.updateuserAddress(user);
		userService.SendRegisterMessage(user.getMailaddress(), "品川書店アカウント情報変更", "あなたのアカウントが変更されました。");
		return "userUpdateComplete";
	}
}
