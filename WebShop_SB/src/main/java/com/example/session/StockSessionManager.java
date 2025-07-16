package com.example.session;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.example.model.Product;
import com.example.model.SearchLog;
import com.example.service.ProductService;
import com.example.service.SearchLogService;

@Component
public class StockSessionManager {

	private static final String STOCK_SESSION_KEY = "stockSession";
	
	private final ProductService productService;
	private final SearchLogService searchLogService;
	
	public StockSessionManager(ProductService productService,SearchLogService searchLogService) {
		this.productService = productService;
		this.searchLogService = searchLogService;
	}

	public StockSession getStockSession(HttpSession session) {
		StockSession stockSession = (StockSession) session.getAttribute(STOCK_SESSION_KEY);
		if (stockSession == null) {
			stockSession = new StockSession();
			session.setAttribute(STOCK_SESSION_KEY, stockSession);
		}
		return stockSession;
	}

	public void updateProductList(HttpSession session, Product product) {
		StockSession stockSession = getStockSession(session);
		List<Product> list = stockSession.getStock();
		
		for (Product p : list) {
			if (p.getId() == product.getId()) {
				p.setTitle(product.getTitle());
				p.setAuthor(product.getAuthor());
				p.setRelease_day(product.getRelease_day());
				p.setStock(product.getStock());
				p.setPrice(product.getPrice());
				p.setStock(product.getStock());
				p.setSales(product.getSales());
				p.setImgLink(product.getImgLink());
				p.setGenre(product.getGenre());
				p.setNotice(product.getNotice());
				break;
			}
		}
		
		stockSession.setStock(list);
	}

	public void clearSearchConditions(HttpSession session) {
		StockSession stockSession = getStockSession(session);
		stockSession.setSearchId(null);
		stockSession.setSearchTitle(null);
		stockSession.setSearchAuthor(null);
		stockSession.setSearchGenre(null);
	}

	public void transferSearchConditionsToModel(HttpSession session, Model model) {
		StockSession stockSession = getStockSession(session);
		model.addAttribute("searchId", stockSession.getSearchId());
		model.addAttribute("searchTitle", stockSession.getSearchTitle());
		model.addAttribute("searchAuthor", stockSession.getSearchAuthor());
		model.addAttribute("searchGenre", stockSession.getSearchGenre());
	}

	public int updateSearchConditions(StockSession stockSession, String username,
			String id, String title, String author, String genre) {
		int parsedId = 0;

		// ID
		if (id != null && !id.isBlank()) {
			try {
				parsedId = Integer.parseInt(id);
				stockSession.setSearchId(id);
				saveSearchLog(username, "stock", parsedId, null, null);
			} catch (NumberFormatException e) {
				stockSession.setSearchId(null);
			}
		} else {
			stockSession.setSearchId(null);
		}

		// タイトル
		if (title != null && !title.isBlank()) {
			stockSession.setSearchTitle(title);
			saveSearchLog(username, "stock", null, title, null);
		} else {
			stockSession.setSearchTitle(null);
		}

		// 著者
		if (author != null && !author.isBlank()) {
			stockSession.setSearchAuthor(author);
			saveSearchLog(username, "stock", null, null, author);
		} else {
			stockSession.setSearchAuthor(null);
		}

		// ジャンル（ログは不要ならスキップ可）
		stockSession.setSearchGenre((genre != null && !genre.isBlank()) ? genre : null);

		return parsedId;
	}

	private void saveSearchLog(String username, String place, Integer id, String title, String author) {
		SearchLog log = new SearchLog();
		log.setUsername(username);
		log.setPlace(place);
		if (id != null)
			log.setId(id);
		if (title != null)
			log.setTitle(title);
		if (author != null)
			log.setAuthor(author);
		searchLogService.saveSearchLog(log);
	}
}
