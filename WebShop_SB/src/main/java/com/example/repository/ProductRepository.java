package com.example.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.Comment;
import com.example.model.Coupon;
import com.example.model.Product;
import com.example.model.SearchLog;
import com.example.model.ShopLocation;
import com.example.model.ViewLog;
import com.example.model.alert_review;
import com.example.model.genre;

@Repository
public class ProductRepository {
	private final JdbcTemplate jdbcTemplate;
	final Logger logger = LoggerFactory.getLogger(Product.class);

	public ProductRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	//公開済み商品取得
	public List<Product> findViewAll() {
		String query = "SELECT * FROM product where view = true";
		return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Product.class));
	}

	//イチオシ商品取得
	public List<Product> findRecommand() {
		String query = "SELECT * FROM product where recommand = true";
		return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Product.class));
	}

	//トップ10取得
	public List<Product> findSalesRank() {
		String query = "SELECT * FROM product WHERE view = true ORDER BY sales DESC LIMIT 10;";
		return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Product.class));
	}

	//全商品取得
	public List<Product> findAll() {
		String query = "SELECT * FROM product";
		return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Product.class));
	}

	//id検索
	public Product findById(int id) {
		String query = "SELECT * FROM product WHERE id = ?";
		List<Product> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Product.class), id);
		return list.isEmpty() ? null : list.get(0);
	}

	//ソート
	public void sortProductList(List<Product> products, String sortrule) {
		if ("recommendation".equals(sortrule)) {
			products.sort(Comparator.comparing(Product::getSales).reversed());
		} else if ("favorite".equals(sortrule)) {
			products.sort(Comparator.comparing(Product::getSales));
		} else {
			products.sort(Comparator.comparing(Product::getId));
		}
	}

	//ユーザーページ検索
	public List<Product> searchByKeyword(String keyword) {
		 if (keyword == null || keyword.isBlank()) {
		        return findViewAll(); // 全件表示など
		    }

		    if (keyword.length() >= 2) {
		        // FULLTEXT + JOIN検索
		        String sql = """
		            SELECT p.* FROM product p
		            LEFT JOIN readings r ON p.id = r.product_id
		            WHERE MATCH(p.title, p.author, p.genre, p.notice) AGAINST (? IN NATURAL LANGUAGE MODE)
		               OR MATCH(r.title_hira, r.title_kana, r.title_romaji,
		                        r.author_hira, r.author_kana, r.author_romaji)
		                        AGAINST (? IN NATURAL LANGUAGE MODE)
		              AND p.view = true
		            ORDER BY MATCH(p.title, p.author, p.genre, p.notice)
		                  AGAINST (? IN NATURAL LANGUAGE MODE) DESC
		        """;
		        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class), keyword, keyword, keyword);
		    } else {
		        // LIKE検索（1文字）
		        String like = "%" + keyword + "%";
		        String sql = """
		            SELECT p.* FROM product p
		            LEFT JOIN readings r ON p.id = r.product_id
		            WHERE (p.title LIKE ? OR p.author LIKE ? OR p.genre LIKE ?)
		               OR (r.title_hira LIKE ? OR r.title_kana LIKE ? OR r.title_romaji LIKE ?
		               OR r.author_hira LIKE ? OR r.author_kana LIKE ? OR r.author_romaji LIKE ?)
		              AND p.view = true
		        """;
		        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class),
		                                  like, like, like,
		                                  like, like, like,
		                                  like, like, like);
		    }
	}

	//購入処理
	public int update(Product product) {
		int stock = 0;
		int sales = 0;
		String query = "SELECT * FROM product WHERE id = ?";
		List<Product> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Product.class), product.getId());
		if (!list.isEmpty()) {
			stock = list.get(0).getStock() - product.getQuantity();
			sales = product.getSales() + product.getQuantity();
		}
		if (stock < 0) {
			return -1; // 在庫が不足している場合
		}
		String update = "UPDATE product SET stock = ?, sales = ? WHERE id = ?";
		return jdbcTemplate.update(update, stock, sales, product.getId());
	}

	//商品追加
	public void insert(Product product) {
		String query = "insert into product(title,author,release_day,price,stock,quantity,genre,imgLink,notice) values(?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(query, product.getTitle(), product.getAuthor(), product.getRelease_day(),
				product.getPrice(), product.getStock(), 0, product.getGenre(), product.getImgLink(),
				product.getNotice());
	}

	//商品削除
	public void delete(int id) {
		String update = "delete from product where id = ?";
		jdbcTemplate.update(update, id);
	}

	//商品情報更新
	public void updateProduct(Product product) {
		String update = "update product set title = ?,author = ?,release_day= ?,stock = ?,quantity = ?,price = ?,sales = ?,genre = ?,imgLink = ?,recommand = ?,notice = ?,view = ? where id = ?";
		jdbcTemplate.update(update, product.getTitle(), product.getAuthor(), product.getRelease_day(),
				product.getStock(), product.getQuantity(), product.getPrice(), product.getSales(), product.getGenre(),
				product.getImgLink(), product.isRecommand(), product.getNotice(), product.isView(), product.getId());

	}

	//全ジャンル取得
	public List<genre> findAllGenre() {
		String query = "select * from genre";
		return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(genre.class));
	}

	//ジャンル追加
	public void insertGenre(String genre, int id) {
		try {
			if (!genre.isBlank() && !genre.isEmpty()) {
				String update = "insert into genre values(?)";
				jdbcTemplate.update(update, genre);
				String change = "update product set genre = ? where id = ?";
				jdbcTemplate.update(change, genre, id);
			}
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
		}
	}

	//ジャンル削除
	public void deleteGenre(String genre, int id) {
		Product product = findById(id);
		if (product.getGenre().equals(genre)) {
			product.setGenre("---");
			updateProduct(product);
		}
		String delete = "delete from genre where genrename = ?";
		jdbcTemplate.update(delete, genre);
	}

	//在庫検索
	public List<Product> findSearchAll(int id, String title, String author, String genre) {
		List<Product> list = new ArrayList<>();
		boolean first = false;

		String query = "select * from product where ";

		if (id != 0) {
			query = query + "id = " + id;
			first = true;
		}

		if (!title.isBlank() && !title.isEmpty()) {
			if (!first) {
				query = query + " title like '%" + title + "%'";
				first = true;
			} else {
				query = query + " and title like '%" + title + "%'";
			}

		}

		if (!author.isBlank() && !author.isEmpty()) {
			if (!first) {
				query = query + " author like '%" + author + "%'";
				first = true;
			} else {
				query = query + " and author like '%" + author + "%'";
			}
		}

		if (!genre.equals("---")) {
			if (!first) {
				query = query + " genre = " + "'" + genre + "'";
				first = true;
			} else {
				query = query + " and genre = " + "'" + genre + "'";
			}
		}

		if (first) {
			list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Product.class));
		} else {
			list = null;
		}

		return list;
	}

	//イチオシを10個に制限
	public boolean CountRecommand() {

		String query = "select count(*) from product where recommand = true";
		int result = jdbcTemplate.queryForObject(query, Integer.class);

		if (result < 10) {
			return true;
		} else {
			return false;
		}
	}

	//レビュー表示 　import　でmodelを追加する
	public List<Comment> TakeReviewRepo(int product_id) {
		List<Comment> takeReview = new ArrayList<>();
		String sql = "SELECT * FROM comment WHERE product_id=?";
		takeReview = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class), product_id);
		return takeReview;
	}

	public List<alert_review> UserName_AlertReviewRepo(String user_send_alert) {
		String sql = "select * from alert_review where user_send_alert=?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(alert_review.class), user_send_alert);
	}

	//レビュー更新　レビューをデータベースに追加
	public void SendReviewRepo(int product_id, String user_name, String writeReview) {
		String sql = "INSERT INTO comment(product_id,user_name,comment) VALUES(?,?,?);";
		jdbcTemplate.update(sql, product_id, user_name, writeReview);
	}

	// 支店情報表示
	public List<ShopLocation> shoplocationGetRepo() {
		List<ShopLocation> shoploca = new ArrayList<>();
		String sql = "SELECT * FROM shoplocation";
		shoploca = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ShopLocation.class));
		return shoploca;
	}

	//閲覧履歴保存
	public void addViewLog(String username, int product_id) {
		String check = "SELECT COUNT(*) FROM viewlog WHERE productid = ? and username = ?";
		int result = jdbcTemplate.queryForObject(check, Integer.class, product_id, username);

		if (result == 0) {
			Product pro = findById(product_id);
			String insert = "INSERT INTO viewlog(username,productid,title,author,release_day,price,genre,imgLink) VALUES(?,?,?,?,?,?,?,?)";
			jdbcTemplate.update(insert, username, product_id, pro.getTitle(), pro.getAuthor(), pro.getRelease_day(),
					pro.getPrice(), pro.getGenre(), pro.getImgLink());
		} else if (result > 0) {
			String update = "UPDATE viewlog SET date = ? WHERE productid = ?";
			jdbcTemplate.update(update, LocalDateTime.now(), product_id);
		}
	}

	//閲覧履歴取り出し
	public List<ViewLog> findAllViewLog(String username) {
		List<ViewLog> list = new ArrayList<>();
		String query = "SELECT * FROM viewlog WHERE username = ? ORDER BY date DESC";
		list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(ViewLog.class), username);
		return list;
	}

	//--検索履歴--
	public void saveSearchLog(SearchLog searchlog) {

		int result = 0;

		//id重複確認
		if (searchlog.getId() != 0) {
			String count = "SELECT COUNT(*) FROM searchlog WHERE id = ? and username = ? and place = ?";
			result = jdbcTemplate.queryForObject(count, Integer.class, searchlog.getId(), searchlog.getUsername(),
					searchlog.getPlace());

			if (result > 0) {
				String update = "UPDATE searchlog SET date = ? WHERE id = ? and username = ? and place = ?";
				jdbcTemplate.update(update, LocalDateTime.now(), searchlog.getId(), searchlog.getUsername(),
						searchlog.getPlace());
			}
		}

		//タイトル重複確認
		if (searchlog.getTitle() != null) {
			String count = "SELECT COUNT(*) FROM searchlog WHERE title = ? and username = ? and place = ?";
			result = jdbcTemplate.queryForObject(count, Integer.class, searchlog.getTitle(), searchlog.getUsername(),
					searchlog.getPlace());

			if (result > 0) {
				String update = "UPDATE searchlog SET date = ? WHERE title = ? and username = ? and place = ?";
				jdbcTemplate.update(update, LocalDateTime.now(), searchlog.getTitle(), searchlog.getUsername(),
						searchlog.getPlace());
			}
		}

		//著者重複確認
		if (searchlog.getAuthor() != null) {
			String count = "SELECT COUNT(*) FROM searchlog WHERE author = ? and username = ? and place = ?";
			result = jdbcTemplate.queryForObject(count, Integer.class, searchlog.getAuthor(), searchlog.getUsername(),
					searchlog.getPlace());

			if (result > 0) {
				String update = "UPDATE searchlog SET date = ? WHERE author = ? and username = ? and place = ?";
				jdbcTemplate.update(update, LocalDateTime.now(), searchlog.getAuthor(), searchlog.getUsername(),
						searchlog.getPlace());
			}
		}

		//ジャンル重複確認
		if (searchlog.getGenre() != null) {
			String count = "SELECT COUNT(*) FROM searchlog WHERE genre = ? and username = ? and place = ?";
			result = jdbcTemplate.queryForObject(count, Integer.class, searchlog.getGenre(), searchlog.getUsername(),
					searchlog.getPlace());

			if (result > 0) {
				String update = "UPDATE searchlog SET date = ? WHERE genre = ? and username = ? and place = ?";
				jdbcTemplate.update(update, LocalDateTime.now(), searchlog.getGenre(), searchlog.getUsername(),
						searchlog.getPlace());
			}
		}

		//重複なしなら追加
		if (result == 0) {
			String insert = "INSERT INTO searchlog(username,place,id,title,author,genre) VALUES(?,?,?,?,?,?)";
			jdbcTemplate.update(insert, searchlog.getUsername(), searchlog.getPlace(), searchlog.getId(),
					searchlog.getTitle(), searchlog.getAuthor(), searchlog.getGenre());
		}
	}

	//在庫管理ページでのid検索欄用の検索履歴
	public List<SearchLog> findSearchIdLog(String username, String place) {
		String query = "SELECT * FROM searchlog WHERE username = ? and place = ? and id != 0 ORDER BY date DESC LIMIT 5";
		List<SearchLog> list = new ArrayList<>();
		list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(SearchLog.class), username, place);
		return list;
	}

	//在庫管理ページでのタイトル検索欄用の検索履歴
	public List<SearchLog> findSearchTitleLog(String username, String place) {
		String query = "SELECT * FROM searchlog WHERE username = ? and place = ? and title != ? ORDER BY date DESC LIMIT 5";
		List<SearchLog> list = new ArrayList<>();
		list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(SearchLog.class), username, place, "null");
		return list;
	}

	//在庫管理ページでの著者検索欄用の検索履歴
	public List<SearchLog> findSearchAuthorLog(String username, String place) {
		String query = "SELECT * FROM searchlog WHERE username = ? and place = ? and author != ? ORDER BY date DESC LIMIT 5";
		List<SearchLog> list = new ArrayList<>();
		list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(SearchLog.class), username, place, "null");
		return list;
	}

	//在庫管理ページでのジャンル検索欄用の検索履歴
	public List<SearchLog> findSearchGenreLog(String username, String place) {
		String query = "SELECT * FROM searchlog WHERE username = ? and place = ? and genre != null ORDER BY date DESC LIMIT 5";
		List<SearchLog> list = new ArrayList<>();
		list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(SearchLog.class), username, place);
		return list;
	}
	//--検索履歴ここまで--

	//クーポン
	public Coupon findByCoupon(String couponcode) {
		String query = "SELECT * FROM coupon where name = ?";
		List<Coupon> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Coupon.class), couponcode);
		return list.isEmpty() ? null : list.get(0);
	}

	//クーポンを使用済みに変更
	public void couponUpdate(String couponcode) {

		String query = "UPDATE coupon SET usedcoupon = true where name = ?";
		System.out.println("couponcode");
		jdbcTemplate.update(query, couponcode);
	}
}