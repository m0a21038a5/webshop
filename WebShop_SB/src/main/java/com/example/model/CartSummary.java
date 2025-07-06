package com.example.model;

public class CartSummary {
	private final int finalPrice;
    private final String message;

    public CartSummary(int finalPrice, String message) {
        this.finalPrice = finalPrice;
        this.message = message;
    }

    public int getFinalPrice() {
    	return finalPrice; 
    }
    
    public String getMessage() { 
    	return message; 
    }
}
