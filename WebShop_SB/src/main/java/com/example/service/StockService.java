package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.Product;
import com.example.model.genre;
import com.example.repository.StockRepository;

@Service
public class StockService {
	private StockRepository stockRepository;
	
	public StockService(StockRepository stockRepository) {
		this.stockRepository = stockRepository;
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
