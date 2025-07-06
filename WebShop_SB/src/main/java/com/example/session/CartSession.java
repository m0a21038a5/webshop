package com.example.session;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Coupon;
import com.example.model.Product;

public class CartSession {
	private List<Product> cart = new ArrayList<>();
    private Integer usePoint = 0;
    private Coupon coupon;
    private Integer sum = 0;

    public List<Product> getCart() {
        return cart;
    }

    public void setCart(List<Product> cart) {
        this.cart = cart;
    }

    public Integer getUsePoint() {
        return usePoint;
    }

    public void setUsePoint(Integer usePoint) {
        this.usePoint = usePoint;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }
}
