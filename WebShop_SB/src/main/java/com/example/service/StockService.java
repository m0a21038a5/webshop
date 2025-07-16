package com.example.service;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.example.model.Product;
import com.example.model.genre;
import com.example.repository.StockRepository;
import com.example.session.StockSession;
import com.example.session.StockSessionManager;

@Service
public class StockService {
	private StockRepository stockRepository;
	private ProductService productService;
	private StockSessionManager stockSessionManager;

	public StockService(StockRepository stockRepository, ProductService productService,
			StockSessionManager stockSessionManager) {
		this.stockRepository = stockRepository;
		this.productService = productService;
		this.stockSessionManager = stockSessionManager;
	}

	public Product setProduct(int id, String title, String author, String release_day, int price, int stock, int sales,
			String imgLink, String genre, String notice) {
		Product product = productService.findById(id);
		product.setTitle(title);
		product.setAuthor(author);
		product.setRelease_day(release_day);
		product.setStock(stock);
		product.setPrice(price);
		product.setStock(stock);
		product.setSales(sales);
		product.setImgLink(imgLink);
		product.setGenre(genre);
		product.setNotice(notice);

		return product;
	}

	public void setDefaultProduct() {
		Product product = new Product();
		product.setTitle("タイトル");
		product.setAuthor("著者");
		product.setRelease_day("発売日");
		product.setStock(0);
		product.setQuantity(0);
		product.setPrice(0);
		product.setSales(0);
		product.setImgLink("画像リンク");
		product.setGenre("ジャンル");
		product.setRecommand(false);
		product.setNotice("説明");
		product.setView(false);
		insert(product);
	}

	public void setRecommand(HttpSession session, List<String> recommand, int id) {
		boolean flag = false;
		if (recommand != null) {
			flag = true;
		}

		if (flag && !CountRecommand()) {
			flag = false;
		}

		Product product = productService.findById(id);
		product.setRecommand(flag);

		StockSession stockSession = stockSessionManager.getStockSession(session);

		List<Product> list = stockSession.getStock();
		updateProduct(product);

		for (Product p : list) {
			if (p.getId() == id) {
				p.setRecommand(flag);
				break;
			}
		}

		stockSession.setStock(list);
	}

	public void setView(HttpSession session, List<String> view, int id) {
		boolean flag = false;
		if (view != null) {
			flag = true;
		}

		Product product = productService.findById(id);
		product.setView(flag);

		StockSession stockSession = stockSessionManager.getStockSession(session);

		List<Product> list = stockSession.getStock();
		updateProduct(product);

		for (Product p : list) {
			if (p.getId() == id) {
				p.setView(flag);
				break;
			}
		}

		stockSession.setStock(list);
	}

	public void insert(Product product) {
		stockRepository.insert(product);
	}

	public void delete(int id) {
		stockRepository.delete(id);
	}

	public void updateProduct(Product product) {
		stockRepository.updateProduct(product);
	}

	public List<genre> findAllGenre() {
		return stockRepository.findAllGenre();
	}

	public void addNewGenre(String genre, int id) {
		stockRepository.insertGenre(genre, id);
	}

	public void deleteGenre(String genre, int id) {
		stockRepository.deleteGenre(genre, id);
	}

	public boolean CountRecommand() {
		return stockRepository.CountRecommand();
	}
}
