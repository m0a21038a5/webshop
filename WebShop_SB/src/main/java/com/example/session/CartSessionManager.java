package com.example.session;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

@Component
public class CartSessionManager {
	private static final String SESSION_KEY = "cartSession";

    public CartSession get(HttpSession session) {
        CartSession cartSession = (CartSession) session.getAttribute(SESSION_KEY);
        if (cartSession == null) {
            cartSession = new CartSession();
            session.setAttribute(SESSION_KEY, cartSession);
        }
        return cartSession;
    }

    public void save(HttpSession session, CartSession cartSession) {
        session.setAttribute(SESSION_KEY, cartSession);
    }

    public void clear(HttpSession session) {
        session.removeAttribute(SESSION_KEY);
    }
}
