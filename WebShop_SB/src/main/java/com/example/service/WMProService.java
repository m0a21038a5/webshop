package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.BeforeProducts;
import com.example.model.BuyLog;
import com.example.model.Product;
import com.example.repository.WMProRepository;

@Service
public class WMProService {

	private WMProRepository wm;

	public WMProService(WMProRepository wm) {
		this.wm = wm;
	}
	//以下追加

	public List<BuyLog> findByAllBuylog() {
		return wm.findByAllBuylog();
	}

	public void updateprice(int price, String title) {
		wm.updateprice(price, title);

	}

	public void backupPrice() {
		wm.backupPrice();

	}

	public void finishedPrice(int price, String title) {
		wm.finishedPrice(price, title);

	}

	public List<BeforeProducts> findAllBeforeProduct() {

		return wm.findAllBeforeProduct();
	}

	public void updateBackupPrice(Product product) {
		wm.updateBackupPrice(product);
	}

}
