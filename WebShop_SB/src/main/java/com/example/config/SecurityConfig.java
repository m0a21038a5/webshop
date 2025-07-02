package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.service.CustomAuthenticationSuccessHandler;

@Configuration // このクラスがSpringの設定クラスであることを示す
@EnableWebSecurity // Spring Securityの機能を有効にする
public class SecurityConfig {

	@Autowired
	CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;


	@Bean // Springコンテナにこのメソッドの戻り値をBeanとして登録
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.formLogin(login->{
    		login.loginPage("/login").permitAll()
    		.successHandler(customAuthenticationSuccessHandler)
    		.usernameParameter("username")
    		.passwordParameter("password")
    		.failureUrl("/failure").permitAll();
    	})
    	.authorizeHttpRequests(auth->{
    		auth.requestMatchers("/makehash").permitAll()	
    			.requestMatchers("/addproduct","/addproductInsert","/addsyouhin","/delete2","/admin/**","/Stock","/timesale","/answer","/answercomplete","/UserInfo").hasRole("ADMIN")//追加
    			.requestMatchers("/products/**").permitAll()
    			.requestMatchers("/user/**").permitAll()
    			.requestMatchers("/register").permitAll()
    			.requestMatchers("/registrationComplete").permitAll()
    			.requestMatchers("/NewUser").permitAll()
    			.requestMatchers("/products","/homepage").permitAll()
    			.requestMatchers("/css/**").permitAll()
    		    .requestMatchers("/js/**", "/images/**").permitAll()
    		    .requestMatchers("/speech-to-text").permitAll()  // 音声認識エンドポイントを認証なしにする
    			.requestMatchers("/api/speech/recognize").permitAll()
    		    .requestMatchers("/wav/**").permitAll() 		    
    		    .requestMatchers("/ShopLocation").permitAll() 		    
    			.anyRequest().authenticated();
    	}).logout(logout->logout.logoutUrl("/logout")
    	  .invalidateHttpSession(true)
    	  .clearAuthentication(true)
    	  .logoutSuccessUrl("/login"));
		
		http.csrf(AbstractHttpConfigurer::disable);//追加

		return http.build(); // HttpSecurityの設定をもとにSecurityFilterChainを構築して返す
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		//BCryptアルゴリズムを使用してパスワードのハッシュ化を行う
		return new BCryptPasswordEncoder(); // --- (2) BCryptアルゴリズムを使用
	}
}
