package com.example.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.model.Coupon;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.BuyLogService;
import com.example.service.CouponService;
import com.example.service.MailService;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.example.session.CartSession;
import com.example.session.CartSessionManager;

@Controller
public class BuyController {
	private final ProductService productService;
	private final UserService userService;
	private final CartSessionManager cartSessionManager;
	private final MailService mailService;
	private final CouponService couponService;
	private final BuyLogService buyLogService;

	public BuyController(ProductService productService, UserService userService, CartSessionManager cartSessionManagers,
			MailService mailService,CouponService couponService,BuyLogService buyLogService) {
		this.productService = productService;
		this.userService = userService;
		this.cartSessionManager = cartSessionManagers;
		this.mailService = mailService;
		this.couponService = couponService;
		this.buyLogService = buyLogService;
	}

	//購入ページ
	@PostMapping("/buy")
	public String buy() {
		return "redirect:/buyComplete";
	}

	//購入処理
	@GetMapping("/buyComplete")
	public String buyComplete(Model model, HttpSession session,
			@AuthenticationPrincipal UserDetails userDetails) {

		CartSession cartSession = cartSessionManager.get(session);
		List<Product> cart = cartSession.getCart();

		//カート変更
		int result = productService.update(cart);

		if (result > 0) {
			String username = userDetails.getUsername();
			mailService.setCartMessage(userService.findByUsername(username).getMailaddress(), "購入完了メール", cartSession);
			for (Product p : cart) {
				buyLogService.insertLog(username, p);
			}

			int sum = cartSession.getSum();

			//所持ポイントから使用ポイントを引いてそれに還元ポイントを足す処理
			User user = userService.findByUsername(username);
			int userpoint = user.getPoint();
			Object usepointObj = cartSession.getUsePoint();
			int usepoint = (usepointObj instanceof Integer) ? (int) usepointObj : 0;
			int point = (int) ((userpoint - usepoint) + sum * 0.1);
			user.setPoint(point);

			//クーポンを使用済みに更新
			if (cartSession.getCoupon() != null) {
				Coupon coupon = cartSession.getCoupon();
				couponService.couponUpdate(coupon.getName());
			}

			userService.point(user);

			cartSessionManager.clear(session);
			return "buyComplete";
		} else {
			return "redirect:/cart";
		}
	}
}
