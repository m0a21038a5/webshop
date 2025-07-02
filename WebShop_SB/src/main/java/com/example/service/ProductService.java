package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.Comment;
import com.example.model.Coupon;
import com.example.model.Product;
import com.example.model.SearchLog;
import com.example.model.ShopLocation;
import com.example.model.ViewLog;
import com.example.model.alert_review;
import com.example.model.genre;
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

	public List<Product> findByTitle(String title) {
		return productRepository.findByTitle(title);
	}

	public List<Product> findByAuthor(String author) {
		return productRepository.findByAuthor(author);
	}
	
	public List<Product> findByGenre(String genre){
		return productRepository.findByGenre(genre);
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

	public void insert(Product product) {
		productRepository.insert(product);
	}

	public void delete(int id) {
		productRepository.delete(id);
	}

	public void updateProduct(Product product) {
		productRepository.updateProduct(product);
	}

	public List<genre> findAllGenre() {
		return productRepository.findAllGenre();
	}

	public void addNewGenre(String genre, int id) {
		productRepository.insertGenre(genre, id);
	}

	public void deleteGenre(String genre, int id) {
		productRepository.deleteGenre(genre, id);
	}

	public List<Product> findSearchAll(int id, String name, String author, String genre) {
		return productRepository.findSearchAll(id, name, author, genre);
	}
	
	//公開済み商品取得
	public List<Product> findViewAll() {
		return productRepository.findViewAll();
	}
	
	//イチオシ商品取得
	public List<Product> findRecommand(){
		return productRepository.findRecommand();
	}
	
	//売り上げランキングトップ10
	public List<Product> findSalesRank(){
		return productRepository.findSalesRank();
	}

	// 検索 清水 追加
	public List<Product> search(String genre, String author, String title) {
		return productRepository.search(genre, author, title);
	}

	public boolean CountRecommand() {
		return productRepository.CountRecommand();
	}

//////ここから　0612中村追加　修正済み
	  //レビュー表示

	  public List<Comment> TakeReview(int product_id, String user_send_alert) {
	  	List<Comment> takeReview = productRepository.TakeReviewRepo(product_id);
	  	List<alert_review> AlertReview = productRepository.UserName_AlertReviewRepo(user_send_alert);
	  	
	  	List<String> TakeUserAlertName = new ArrayList<>();
	  	for(alert_review alertRe : AlertReview) {
	  		String alna = alertRe.getUser_alert();
	  		TakeUserAlertName.add(alna);
	  	}
	  	
		for(Comment AlRecome : takeReview) {
//			AlRecome.setAlreadyAlert(false);
			String alre = AlRecome.getUser_name();
			if(TakeUserAlertName.contains(alre)) {
				AlRecome.setAlreadyAlert(true);
			}
		}
	  	return takeReview;
	  }

	  
	  //レビュー更新
		public void SendReview(int product_id, String user_name, String writeReview) {
			productRepository.SendReviewRepo(product_id, user_name, writeReview);
		}
///////ここまで　0612中村追加　修正済み

//////ここから　0615中村追加　位置情報機能
	// 支店情報表示
	public List<ShopLocation> shoplocationGet() {
		List<ShopLocation> shoploca = productRepository.shoplocationGetRepo();
		return shoploca;
	}

	// 現在位置取得
	public List<ShopLocation> shoploSe(String lat, String lng) {
		List<ShopLocation> shoploca = productRepository.shoplocationGetRepo();
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
//////ここまで　0615中村追加　位置情報機能
	//閲覧履歴保存
	public void addViewLog(String username, int product_id) {
		productRepository.addViewLog(username, product_id);
	}

	public List<ViewLog> findAllViewLog(String username) {
		return productRepository.findAllViewLog(username);
	}

	//検索履歴追加
	public void saveSearchLog(SearchLog searchlog) {
		productRepository.saveSearchLog(searchlog);
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
