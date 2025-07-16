package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.BuyLog;
import com.example.model.Product;
import com.example.repository.BuyLogRepository;

@Service
public class BuyLogService {
	
	private final BuyLogRepository buyLogRepository;
	
	public BuyLogService(BuyLogRepository buyLogRepository) {
		this.buyLogRepository = buyLogRepository;
	}
	
	public void insertLog(String username, Product p) {
		buyLogRepository.insertLog(username, p);

	}

	public List<BuyLog> findByLog(String username) {
		return buyLogRepository.findByLog(username);
	}
}
