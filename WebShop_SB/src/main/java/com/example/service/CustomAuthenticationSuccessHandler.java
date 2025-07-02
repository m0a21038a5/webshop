package com.example.service;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
    	
    		//ロール判定
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // まず保存されたリクエストを取得
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        //ログイン必須のページへアクセスした場合
        if (savedRequest != null) {
        	 	//adminアクセス時は管理者ページへ
            if (isAdmin) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect(savedRequest.getRedirectUrl());
            }
        } else {
            //ロール判定によるリダイレクト
            if (isAdmin) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/homepage");
            }
        }
    }
}
