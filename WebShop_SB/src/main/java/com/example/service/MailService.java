package com.example.service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.example.session.CartSession;

@Service
public class MailService {
	private final JavaMailSender javaMailSender;

	public MailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	//購入完了メール
	public void setCartMessage(String mailaddress, String about, CartSession cartSession) {
		final SpringTemplateEngine engine = new SpringTemplateEngine();
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setCharacterEncoding("UTF-8");
		engine.setTemplateResolver(templateResolver);

		//htmlへ情報を保存
		final Map<String, Object> variables = new HashMap<>();
		variables.put("h1_text", about);
		variables.put("products", cartSession.getCart());
		variables.put("sum", cartSession.getSum() - cartSession.getUsePoint());

		final Context context = new Context();
		context.setVariables(variables);

		final String htmlBody = engine.process("mail/html/htmlmail.html", context);

		SendMessage(mailaddress,about,htmlBody);
	}

	//ユーザー登録完了メール
	public void setRegisterMessage(String mailaddress, String about, String message) {
		final SpringTemplateEngine engine = new SpringTemplateEngine();
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setCharacterEncoding("UTF-8");
		engine.setTemplateResolver(templateResolver);

		final Map<String, Object> variables = new HashMap<>();
		variables.put("about", about);
		variables.put("message", message);

		final Context context = new Context();
		context.setVariables(variables);

		final String htmlBody = engine.process("mail/html/htmlregister.html", context);

		SendMessage(mailaddress,about,htmlBody);
	}
	
	private void SendMessage(String mailaddress, String about, String htmlBody) {
		try {
			final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
			// MimeMessageHelper 第2引数でマルチパート指定
			final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
			helper.setFrom("springhost415@gmail.com");
			helper.setTo(mailaddress);
			helper.setSubject(about);
			helper.setText(htmlBody, true);

			this.javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {

		}
	}
}
