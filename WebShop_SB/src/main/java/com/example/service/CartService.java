package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.example.model.CartSummary;
import com.example.model.Coupon;
import com.example.model.Product;

@Service
public class CartService {
	 public List<Product> getCart(HttpSession session) {
	        return Optional.ofNullable((List<Product>) session.getAttribute("cart")).orElse(new ArrayList<>());
	    }

	    public int getUsePoint(HttpSession session) {
	        return Optional.ofNullable((Integer) session.getAttribute("usepoint")).orElse(0);
	    }

	    public Coupon getCoupon(HttpSession session) {
	        return (Coupon) session.getAttribute("couponcode");
	    }

	    public int calculateTotal(List<Product> cart) {
	        return cart.stream().mapToInt(p -> p.getPrice() * p.getQuantity()).sum();
	    }

	    public CartSummary applyCouponAndPoints(List<Product> cart, int usePoint, Coupon coupon, int sumBefore) {
	        int total = sumBefore - usePoint;
	        
	     
	        
	        String message = null;

	        if (coupon != null) {
	            if (coupon.isUsedcoupon()) {
	                message = "このクーポンは使われています";
	            } else if (total - coupon.getDiscount() < 0) {
	                message = "上限を超えています";
	            } else {
	                total -= coupon.getDiscount();
	                if (coupon.getName() != null) {
	                    message = "クーポン" + coupon.getName() + "を使用しました";
	                }
	            }
	        }

	        return new CartSummary(total, message);
	    }
}
