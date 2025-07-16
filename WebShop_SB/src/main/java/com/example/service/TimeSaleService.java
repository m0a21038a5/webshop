package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.BeforeProducts;
import com.example.model.Product;
import com.example.repository.TimeSaleRepository;

@Service
public class TimeSaleService {
	
	private TimeSaleRepository timeSaleRepository;
	
	public TimeSaleService(TimeSaleRepository timeSaleRepository) {
		this.timeSaleRepository = timeSaleRepository;
	}
	
	public void updateprice(int price, String title) {
		timeSaleRepository.updateprice(price, title);

	}

	public void backupPrice() {
		timeSaleRepository.backupPrice();

	}

	public void finishedPrice(int price, String title) {
		timeSaleRepository.finishedPrice(price, title);

	}

	public List<BeforeProducts> findAllBeforeProduct() {

		return timeSaleRepository.findAllBeforeProduct();
	}

	public void updateBackupPrice(Product product) {
		timeSaleRepository.updateBackupPrice(product);
	}
}
