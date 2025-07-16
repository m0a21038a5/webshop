package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.Product;
import com.example.repository.ProductRepository;

@Service
public class ProductService {

	private ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public Product findById(int id) {
		return productRepository.findById(id);
	}

	public void sortProductList(List<Product> products, String rank) {
		productRepository.sortProductList(products, rank);
	}

	public int update(List<Product> list) {
		int result = 0;
		int count = 0;
		for (Product pro : list) {
			result = productRepository.update(pro);
			if (result < 0) {
				return -1;
			}
			count++;
		}
		return count;
	}

	//公開済み商品取得
	public List<Product> findViewAll() {
		return productRepository.findViewAll();
	}

	//イチオシ商品取得
	public List<Product> findRecommand() {
		return productRepository.findRecommand();
	}

	//売り上げランキングトップ10
	public List<Product> findSalesRank() {
		return productRepository.findSalesRank();
	}

}
