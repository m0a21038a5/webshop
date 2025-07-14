package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.ShopLocation;
import com.example.repository.ShopLocationRepository;

@Service
public class ShopLocationService {
	
	private final ShopLocationRepository shopLocationRepository;
	
	public ShopLocationService(ShopLocationRepository shopLocationRepository) {
		this.shopLocationRepository = shopLocationRepository;
	}
	
	public List<ShopLocation> shoplocationGet() {
		List<ShopLocation> shoploca = shopLocationRepository.shoplocationGetRepo();
		return shoploca;
	}

	// 現在位置取得
	public List<ShopLocation> shoploSe(String lat, String lng) {
		List<ShopLocation> shoploca = shopLocationRepository.shoplocationGetRepo();
		double lati = Double.parseDouble(lat);
		double lngi = Double.parseDouble(lng);
		List<ShopLocation> GetShopLo = new ArrayList<>();

		for (ShopLocation ShLo : shoploca) {
			double doubleshoplocationLatitude = ShLo.getLatitude();
			double doubleshoplocationLongitude = ShLo.getLongitude();
			double diffeLat = 0;
			double diffeLng = 0;

			// 緯度の差を算出
			if (lati > doubleshoplocationLatitude) {
				diffeLat = lati - doubleshoplocationLatitude;
			} else {
				diffeLat = doubleshoplocationLatitude - lati;
			}

			// 経度の差を算出
			if (lngi > doubleshoplocationLongitude) {
				diffeLng = lngi - doubleshoplocationLongitude;
			} else {
				diffeLng = doubleshoplocationLongitude - lngi;
			}

			if (diffeLat < 0.05 && diffeLng < 0.05) {
				GetShopLo.add(ShLo);
			}
		}
		return GetShopLo;
	}
}
