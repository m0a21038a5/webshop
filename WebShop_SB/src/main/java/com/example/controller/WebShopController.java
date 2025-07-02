package com.example.controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

import com.example.model.BuyLog;
import com.example.model.Comment;
import com.example.model.Coupon;
import com.example.model.Product;
import com.example.model.SearchLog;
import com.example.model.User;
import com.example.service.CommentService;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.example.service.WMProService;

@Controller
public class WebShopController {
	private WMProService wm;
	private final UserService userService;
	private final ProductService productService;
	private final CommentService commentService;
	//private final SpeechClient speechClient;

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
		List<Product> productList = productService.findViewAll();
		List<Product> favoriteProducts = productList.stream().filter(Product::getFabolit)
				.sorted(Comparator.comparing(Product::getQuantity)).collect(Collectors.toList());

		model.addAttribute("imagePaths", favoriteProducts.isEmpty() ? productList : favoriteProducts);
		//履歴追加
		if (SecurityContextHolder.getContext().getAuthentication().getName() != null) {
			model.addAttribute("searchList", productService
					.findSearchTitleLog(SecurityContextHolder.getContext().getAuthentication().getName(), "products"));
		} else {
			model.addAttribute("searchList", null);
		}

		//イチオシリスト
		model.addAttribute("recommend", productService.findRecommand());
		//ランキング
		List<Product> rank = new ArrayList<>();
		rank = productService.findSalesRank();
		int num = 1;
		for (Product p : rank) {
			p.setRank(num);
			num++;
		}
		model.addAttribute("rank", rank);
		model.addAttribute("loginUser",SecurityContextHolder.getContext().getAuthentication().getName());
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
			@RequestParam(value = "rank", required = false) String rank,
			@RequestParam(value = "search", required = false) String search) {

		List<Product> productList = new ArrayList<>(); // 初期化

		// 検索条件の前処理
		if (search != null) {
			// 不要な単語を取り除く（例: "検索 "）
			search = search.replaceAll("検索", "").trim();
		}

		// 検索条件に基づく商品リストの取得
		if (search != null && !search.isBlank()) {
			productList = productService.findByTitle(search);

			//履歴保存
			if (SecurityContextHolder.getContext().getAuthentication().getName() != "anonymousUser") {
				SearchLog searchlog = new SearchLog();
				searchlog.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
				searchlog.setPlace("products");
				searchlog.setTitle(search);
				productService.saveSearchLog(searchlog);
			}

			// タイトルでの検索結果が空の場合、著者名で検索
			if (productList.isEmpty()) {
				productList = productService.findByAuthor(search);
				
				if (productList.isEmpty()) {
					productList = productService.findByGenre(search);
				}
			}
		}

		// どちらの検索でも結果が得られない場合、全ての商品を取得
		if (productList.isEmpty()) {
			productList = productService.findViewAll();
		}

		// ランクに基づく並び替え
		if ("recommendation".equals(rank)) {
			productList.sort(Comparator.comparing(Product::getSales).reversed());
		} else if ("fabolit".equals(rank)) {
			productList.sort(Comparator.comparing(Product::getSales));
		} else {
			productList.sort(Comparator.comparing(Product::getId));
		}

		model.addAttribute("products", productList);
		model.addAttribute("search", search);
		model.addAttribute("rank", rank);

		//履歴追加
		if (SecurityContextHolder.getContext().getAuthentication().getName() != "anonymousUser") {
			model.addAttribute("searchList", productService
					.findSearchTitleLog(SecurityContextHolder.getContext().getAuthentication().getName(), "products"));
		} else {
			model.addAttribute("searchList", null);
		}
		model.addAttribute("loginUser",SecurityContextHolder.getContext().getAuthentication().getName());
		return "products";
	}

	//検索 清水追加
	@PostMapping("/search")
	public String search(@RequestParam(value = "author", required = false) String author,
			@RequestParam(value = "genre", required = false) String genre,
			@RequestParam(value = "title", required = false) String title, Model model) {
		model.addAttribute("products", productService.search(genre, author, title));

		model.getAttribute(genre);
		model.getAttribute(author);
		model.getAttribute(title);
		return "products";
	}

	@GetMapping("/products/{id}")
	public String showProductDetail(@PathVariable("id") int product_id, Model model,@AuthenticationPrincipal UserDetails userDetails) {
		Product product = productService.findById(product_id);
		Comment stringComment = commentService.findAllReviewId(product_id);

		//閲覧履歴保存
		if (SecurityContextHolder.getContext().getAuthentication().getName() != "anonymousUser") {
			productService.addViewLog(SecurityContextHolder.getContext().getAuthentication().getName(), product_id);
		}
		if (product == null) {
			return "redirect:/404";
		}
		if (stringComment == null) {
			model.addAttribute("TakeReview", "レビューはまだありません");
		} else {
			model.addAttribute("TakeReview", stringComment);
		}
		List<Product> productList = productService.findViewAll();

		model.addAttribute("product", product);
		model.addAttribute("productList", productList);
		System.out.println("product.title = " + (product != null ? product.getTitle() : "null"));

	/////ここから　0612中村追加　レビュー表示	import com.example.model.Comment　を追加しました
			String user_send_alert = SecurityContextHolder.getContext().getAuthentication().getName();		//通報者　かつ　ログインユーザー
			model.addAttribute("Review_user_name", user_send_alert);
			model.addAttribute("TakeReview", productService.TakeReview(product_id,  user_send_alert));
			/////ここまで　0612中村追加　レビュー表示
			
			/////ここから　0614中村追加　サンプルページ
			String str = "/images/" + product_id;
			for (int i = 1; i <= 3; i++) {
				if (i == 1) {
					model.addAttribute("SampleFileDirectory1", str + "/Sample" + 1 + ".png");
				}
				if (i == 2) {
					model.addAttribute("SampleFileDirectory2", str + "/Sample" + 2 + ".png");
				}
				if (i == 3) {
					model.addAttribute("SampleFileDirectory3", str + "/Sample" + 3 + ".png");
				}
			}
			///// ここまで 0614中村追加 サンプルページ
			model.addAttribute("loginUser",SecurityContextHolder.getContext().getAuthentication().getName());
			return "detail";
		}

	 
		///// ここから 0612中村追加 レビュー更新
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
		/////ここまで　0612中村追加　レビュー更新
		
		/////ここから　0618中村追加　通報
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
		/////ここまで　0618中村追加　通報

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
			newUser.setPassword(password); // プレーンテキストで保存（ハッシュ化を推奨）
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

	//クーポン機能追加 清水
	@GetMapping("/cart")
	public String viewCart(HttpSession session, Model model, @AuthenticationPrincipal UserDetails userDetails) {
		//現在の所持ポイント
		String username = userDetails.getUsername();

		User user = userService.findByUsername(username);
		int userPoint = user.getPoint();

		List<Product> cart = (List<Product>) session.getAttribute("cart");
		int sum = 0;

		if (cart != null) {
			for (Product product : cart) {
				sum += product.getPrice() * product.getQuantity();
			}
		}

		if (session.getAttribute("usepoint") == null) {
			session.setAttribute("usepoint", 0);
		}

		Coupon coupon = new Coupon();

		//nullチェック
		if (session.getAttribute("couponcode") != null) {
			coupon = (Coupon) session.getAttribute("couponcode");
		}

		int usepoint = (int) session.getAttribute("usepoint");
		userPoint -= usepoint;
		if (coupon != null) {
			if (coupon.isUsedcoupon() == false) {
				if ((sum - usepoint) - coupon.getDiscount() < 0) {
					model.addAttribute("error", "上限を超えています");
				} else {
					sum = (sum - usepoint) - coupon.getDiscount();
					if (coupon.getName() != null) {
						model.addAttribute("error", "クーポン" + coupon.getName() + "を使用しました");
					}
				}
			} else {
				model.addAttribute("error", "このクーポンは使われています");
			}
		} else {
			model.addAttribute("error", "クーポンIDが違います");
		}


		//ここからレコメンド
		List<BuyLog> allList = new ArrayList<>();
		allList = wm.findByAllBuylog();

		List<List<Integer>> recommendBefore = new ArrayList<>();
		List<List<Double>> cos = new ArrayList<>();
		List<Product> all = productService.findAll();
		List<Integer> ID = new ArrayList<>();

		for (Product p : all) {
			ID.add(p.getId());
		}

		for (BuyLog buylog : allList) {
			List<Integer> reclist = new ArrayList<>();
			for (int j = 0; j < productService.findAll().size(); j++) {
				if (ID.get(j) == buylog.getProductid()) {
					reclist.add(buylog.getQuantity());
				} else {
					reclist.add(0);
				}
			}
			recommendBefore.add(reclist);
		}

		for (int i = 0; i < recommendBefore.size(); i++) {
			for (int j = i + 1; j < recommendBefore.size(); j++) {

				if (allList.get(i).getUsername().equals(allList.get(j).getUsername())) {
					for (int k = 0; k < productService.findAll().size(); k++) {

						recommendBefore.get(i).set(k, recommendBefore.get(i).get(k) + recommendBefore.get(j).get(k));
					}
					allList.remove(j);
					recommendBefore.remove(j);
					j--;
				}

			}
		}

		for (List<Integer> a : recommendBefore) {
			for (int b : a) {
				System.out.print(b);
			}
			System.out.println();
		}

		//レコメンド前処理

		for (int i = 0; i < productService.findAll().size(); i++) {
			List<Double> cosRow = new ArrayList<>();
			for (int j = 0; j < productService.findAll().size(); j++) {
				double dot = 0;
				double normA = 0;
				double normB = 0;
				for (int k = 0; k < recommendBefore.size(); k++) {

					dot = dot + (recommendBefore.get(k).get(i) * recommendBefore.get(k).get(j));
					normA = normA + (recommendBefore.get(k).get(i) * recommendBefore.get(k).get(i));
					normB = normB + (recommendBefore.get(k).get(j) * recommendBefore.get(k).get(j));

				}
				if (Double.isNaN(dot / (Math.sqrt(normA) * Math.sqrt(normB)))) {
					cosRow.add(0.0);
				} else {
					cosRow.add(dot / (Math.sqrt(normA) * Math.sqrt(normB)));
				}
			}
			cos.add(cosRow);
		}

		//cos類似度行列表示
		for (List<Double> list1 : cos) {
			for (Double a : list1) {
				System.out.print(String.format("%.2f", a));
				System.out.print(String.format("   "));
			}
			System.out.println();
		}

		List<Double> recCart = new ArrayList<>();

		for (int i = 0; i < productService.findAll().size(); i++) {
			recCart.add(0.0);
		}

		if (cart != null) {
			for (Product p : cart) {
				for (int i = 0; i < productService.findAll().size(); i++) {
					if (ID.get(i) == p.getId()) {
						recCart.set(i, (double) p.getQuantity());
					}
				}
			}
		}

		for (double d : recCart) {
			System.out.println(d);
		}

		//ここからレコメンドする商品を選ぶ処理
		List<Integer> rewardIndex = new ArrayList<>();
		List<Double> result = new ArrayList<>();
		List<Integer> resultindex = new ArrayList<>();
		Double max = 0.0;
		Double secondmax = 0.0;
		int secondmaxindex = 0;
		int maxindex = 0;

		//レコメンドできる商品(カートに入れていない商品)のインデックスを取得
		for (int i = 0; i < recCart.size(); i++) {
			if (recCart.get(i) == 0.0) {
				rewardIndex.add(i);
			}
		}
		if (rewardIndex.isEmpty()) {
			rewardIndex.add(-1);
		}

		//レコメンドできる商品がない(全ての商品をカートに入れている)場合、処理をしない
		if (rewardIndex.get(0) != -1) {

			for (int j = 0; j < rewardIndex.size(); j++) {
				max = 0.0;
				secondmax = 0.0;
				secondmaxindex = 0;
				maxindex = 0;
				//類似度が高い商品2つのインデックスと類似度を取得
				for (int i = 0; i < cos.get(rewardIndex.get(j)).size(); i++) {
					Double value = cos.get(rewardIndex.get(j)).get(i);
					if (value > max && !String.format("%.1f", cos.get(rewardIndex.get(j)).get(i)).equals("1.0")) {
						max = value;
						maxindex = i;
					}
				}

				for (int i = 0; i < cos.get(rewardIndex.get(j)).size(); i++) {
					Double value = cos.get(rewardIndex.get(j)).get(i);

					if (value > secondmax && max > value
							&& !String.format("%.1f", cos.get(rewardIndex.get(j)).get(i)).equals("1.0")) {
						secondmax = value;
						secondmaxindex = i;

					}
				}

				//加重平均を取得して、評価値を算出
				resultindex.add(rewardIndex.get(j));
				result.add(((recCart.get(maxindex) * max) + (recCart.get(secondmaxindex) * secondmax))
						/ (secondmax + max));

			}
		}

		boolean flag = false;

		for (double d : result) {
			if (d != 0.0) {
				flag = true;
			}
		}

		if (flag == true) {
			int res = result.indexOf(Collections.max(result));
			List<Product> list = productService.findAll();
			model.addAttribute("recommendProduct", list);
			model.addAttribute("recommendId", (resultindex.get(res) + 1));
		} else {
		}		

		model.addAttribute("userPoint", userPoint);
		model.addAttribute("cart", cart);
		model.addAttribute("pointbefore", (int) (sum * 0.1));
		model.addAttribute("sum", sum);
		model.addAttribute("loginUser",SecurityContextHolder.getContext().getAuthentication().getName());
		session.setAttribute("sum", sum);
		return "cart"; // カートの表示用テンプレートを返す
	}

	//クーポン機能 清水追加
	@PostMapping("/coupon")
	public String coupon(Model model, HttpSession session,
			@RequestParam(value = "couponcode", required = false) String couponcode) {
		Coupon coupon = productService.findByCoupon(couponcode);
		session.setAttribute("couponcode", coupon);
		return "redirect:/cart";
	}

	// ポイント ポスト清水追加
	@PostMapping("/usepoint")
	public String usepoint(Model model, @RequestParam("usepoint") int usepoint, HttpSession session,
			@AuthenticationPrincipal UserDetails userDetails) {
		//現在の所持ポイント
		String username = userDetails.getUsername();
		User user = userService.findByUsername(username);
		int userPoint = user.getPoint();

		userPoint -= usepoint;

		if (userPoint >= 0) {
			session.setAttribute("usepoint", usepoint);
		} else {
			model.addAttribute("error", "ポイントが足りません");
		}
		return "redirect:/cart";

	}

	@PostMapping("/cart/add")
	public String addToCart(@RequestParam("id") int productId, HttpSession session,
			@AuthenticationPrincipal UserDetails userDetails) {
		Product product = productService.findById(productId);
		if (product != null) {
			List<Product> cart = (List<Product>) session.getAttribute("cart");
			if (cart == null) {
				cart = new ArrayList<>();
			}

			boolean found = false;
			for (Product p : cart) {
				if (p.getId() == product.getId()) {
					p.setQuantity(p.getQuantity() + 1);
					found = true;
					break;
				}
			}

			if (!found) {
				product.setQuantity(1);
				cart.add(product);
			}

			session.setAttribute("cart", cart);
		}
		// カートページにリダイレクト
		return "redirect:/cart";
	}

	@PostMapping("/cart/update")
	public String updateCart(@RequestParam("productId") int productId, @RequestParam("quantity") int quantity,
			HttpSession session) {
		List<Product> cart = (List<Product>) session.getAttribute("cart");
		if (cart != null) {
			for (Product product : cart) {
				if (product.getId() == productId) {
					product.setQuantity(quantity); // 数量を更新
					break;
				}
			}
			session.setAttribute("cart", cart); // 更新したカートをセッションに保存
		}
		return "redirect:/cart"; // カートページにリダイレクト
	}

	@PostMapping("/cart/remove")
	public String removeFromCart(@RequestParam("productId") int productId, HttpSession session) {
		List<Product> cart = (List<Product>) session.getAttribute("cart");
		if (cart != null) {
			cart.removeIf(product -> product.getId() == productId); // 指定されたIDの商品を削除
			session.setAttribute("cart", cart); // 更新したカートをセッションに保存
			session.removeAttribute("sum");
			session.removeAttribute("coupon");
		}

		return "redirect:/cart"; // カートページにリダイレクト
	}

	@PostMapping("/buy")
	public String buy(Model model, HttpSession session,
			@AuthenticationPrincipal UserDetails userDetails) {

		List<Product> cart = (List<Product>) session.getAttribute("cart");

		int result = productService.update(cart);

		if (result > 0) {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			userService.SendCartMessage(userService.findByUsername(name).getMailaddress(), "購入完了メール", cart);
			for (Product p : cart) {
				userService.insertLog(userDetails.getUsername(), p);
			}

			int sum = (int) session.getAttribute("sum");

			//所持ポイントから使用ポイントを引いてそれに還元ポイントを足す処理
			String username = userDetails.getUsername();
			User user = userService.findByUsername(username);
			int userpoint = user.getPoint();
			int usepoint = (int) session.getAttribute("usepoint");
			int point = (int) ((userpoint - usepoint) + sum * 0.1);
			user.setPoint(point);

			//クーポンを使用済みに更新
			if (session.getAttribute("couponcode") != null) {
				Coupon coupon = (Coupon) session.getAttribute("couponcode");
				System.out.println("couponcode");
				productService.couponUpdate(coupon.getName());
			}

			userService.point(user);

			session.removeAttribute("couponcode");
			session.removeAttribute("cart");
			session.removeAttribute("usepoint");
			return "/buyComplete";
		} else {
			return "redirect:/products";
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

	// 公開・非公開
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
 
	// 検索
	@PostMapping("/searchStock")
	public String searchStock(@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "author", required = false) String author,
			@RequestParam(value = "genre", required = false) String genre, Model model, HttpSession session) {

		session.setAttribute("genre", genre);

		model.addAttribute("genrelist", productService.findAllGenre());

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

	// 現在位置取得
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
