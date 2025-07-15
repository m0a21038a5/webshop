package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.Coupon;
import com.example.model.Product;
import com.example.model.SearchLog;
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

	public List<Product> searchByKeyword(String keyword) {
		return productRepository.searchByKeyword(keyword);
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

	public List<Product> findSearchAll(int id, String name, String author, String genre) {
		return productRepository.findSearchAll(id, name, author, genre);
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

	//検索履歴追加
	public void saveSearchLog(SearchLog searchLog) {
		productRepository.saveSearchLog(searchLog);
	}

	public List<SearchLog> findSearchIdLog(String username, String place) {
		return productRepository.findSearchIdLog(username, place);
	}

	public List<SearchLog> findSearchTitleLog(String username, String place) {
		return productRepository.findSearchTitleLog(username, place);
	}

	public List<SearchLog> findSearchAuthorLog(String username, String place) {
		return productRepository.findSearchAuthorLog(username, place);
	}

	public List<SearchLog> findSearchGenreLog(String username, String place) {
		return productRepository.findSearchGenreLog(username, place);
	}

	public Coupon findByCoupon(String couponcode) {
		return productRepository.findByCoupon(couponcode);
	}

	public void couponUpdate(String couponcode) {
		System.out.println("couponcode");
		productRepository.couponUpdate(couponcode);
	}
}
