package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.CartSummary;
import com.example.model.Coupon;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.RecommendationService;
import com.example.service.UserService;
import com.example.session.CartSession;
import com.example.session.CartSessionManager;

@Controller
public class CartController {

	private final CartService cartService;
	private final RecommendationService recommendationService;
	private final ProductService productService;
	private final UserService userService;
	private final CartSessionManager cartSessionManager;

	public CartController(CartService cartService, RecommendationService recommendationService,
			ProductService productService, UserService userService, CartSessionManager cartSessionManagers) {
		this.cartService = cartService;
		this.recommendationService = recommendationService;
		this.productService = productService;
		this.userService = userService;
		this.cartSessionManager = cartSessionManagers;
	}

	@GetMapping("/cart")
	public String viewCart(HttpSession session, Model model, @AuthenticationPrincipal UserDetails userDetails) {
		String username = userDetails.getUsername();
		User user = userService.findByUsername(username);

		CartSession cartSession = cartSessionManager.get(session);
		List<Product> cart = cartSession.getCart();
		int usePoint = cartSession.getUsePoint();
		Coupon coupon = cartSession.getCoupon();
		int sumBefore = cartService.calculateTotal(cart);

		CartSummary summary = cartService.applyCouponAndPoints(cart, usePoint, coupon, sumBefore);
		cartSession.setSum(summary.getFinalPrice());
		cartSessionManager.save(session, cartSession);

		model.addAttribute("sum", summary.getFinalPrice());
		model.addAttribute("userPoint", user.getPoint() - usePoint);
		model.addAttribute("pointbefore", (int) (summary.getFinalPrice() * 0.1));
		model.addAttribute("cart", cart);
		model.addAttribute("loginUser", username);

		if (summary.getMessage() != null) {
			model.addAttribute("error", summary.getMessage());
		}

		Optional<Product> recommended = recommendationService.recommendProduct(cart, username);
		recommended.ifPresent(p -> {
			model.addAttribute("recommendProduct", productService.findAll());
			model.addAttribute("recommendId", p.getId());
		});

		return "cart";
	}

	//クーポン機能
	@PostMapping("/coupon")
	public String coupon(Model model, HttpSession session,
			@RequestParam(value = "couponcode", required = false) String couponcode) {

		Coupon coupon = productService.findByCoupon(couponcode);
		CartSession cartSession = cartSessionManager.get(session);
		cartSession.setCoupon(coupon);
		cartSessionManager.save(session, cartSession);

		return "redirect:/cart";
	}

	// ポイント
	@PostMapping("/usepoint")
	public String usepoint(Model model, @RequestParam("usepoint") int usepoint, HttpSession session,
			@AuthenticationPrincipal UserDetails userDetails) {

		//現在の所持ポイント
		String username = userDetails.getUsername();
		User user = userService.findByUsername(username);
		int userPoint = user.getPoint();

		userPoint -= usepoint;

		if (userPoint < 0) {
			usepoint = user.getPoint();
		} else if (usepoint < 0) {
			usepoint = 0;
		}

		CartSession cartSession = cartSessionManager.get(session);
		cartSession.setUsePoint(usepoint);
		cartSessionManager.save(session, cartSession);

		return "redirect:/cart";

	}

	//カート追加
	@PostMapping("/cart/add")
	public String addToCart(@RequestParam("id") int productId, HttpSession session,
			@AuthenticationPrincipal UserDetails userDetails) {

		Product product = productService.findById(productId);
		if (product != null) {
			CartSession cartSession = cartSessionManager.get(session);
			List<Product> cart = cartSession.getCart();
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

			cartSession.setCart(cart);
			cartSessionManager.save(session, cartSession);
		}

		// カートページにリダイレクト
		return "redirect:/cart";
	}

	//カート数量変更
	@PostMapping("/cart/update")
	public String updateCart(@RequestParam("productId") int productId, @RequestParam("quantity") int quantity,
			HttpSession session) {
		CartSession cartSession = cartSessionManager.get(session);
		List<Product> cart = cartSession.getCart();
		if (cart != null) {
			for (Product product : cart) {
				if (product.getId() == productId) {
					product.setQuantity(quantity); // 数量を更新
					break;
				}
			}
			cartSessionManager.save(session, cartSession);
		}
		return "redirect:/cart"; // カートページにリダイレクト
	}

	//カート削除
	@PostMapping("/cart/remove")
	public String removeFromCart(@RequestParam("productId") int productId, HttpSession session) {
		CartSession cartSession = cartSessionManager.get(session);
		List<Product> cart = cartSession.getCart();
		if (cart != null) {
			cart.removeIf(product -> product.getId() == productId); // 指定されたIDの商品を削除
			cartSession.setCart(cart);
			cartSession.setSum(cartService.calculateTotal(cart));
		}

		return "redirect:/cart"; // カートページにリダイレクト
	}

}
