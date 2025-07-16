package com.example.service;

import org.springframework.stereotype.Service;

import com.example.model.Coupon;
import com.example.repository.CouponRepository;

@Service
public class CouponService {
	
	private final CouponRepository couponRepository;
	
	public CouponService(CouponRepository couponRepository) {
		this.couponRepository = couponRepository;
	}
	
	public Coupon findByCoupon(String couponcode) {
		return couponRepository.findByCoupon(couponcode);
	}

	public void couponUpdate(String couponcode) {
		System.out.println("couponcode");
		couponRepository.couponUpdate(couponcode);
	}
}
