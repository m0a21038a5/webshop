package com.example.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.Coupon;
import com.example.model.Product;
import com.example.model.SearchLog;

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
			return findViewAll();
		}

		// FULLTEXT検索
		String fulltextSql = """
					        SELECT
				    p.*,
				    (
				        2 * MATCH(p.title) AGAINST (? IN NATURAL LANGUAGE MODE) +
				        1.5 * MATCH(p.author) AGAINST (? IN NATURAL LANGUAGE MODE) +
				        1 * MATCH(p.genre) AGAINST (? IN NATURAL LANGUAGE MODE) +
				        0.5 * MATCH(p.notice) AGAINST (? IN NATURAL LANGUAGE MODE) +

				        1.5 * MATCH(r.title_hira, r.title_kana, r.title_romaji) AGAINST (? IN NATURAL LANGUAGE MODE) +
				        1.5 * MATCH(r.author_hira, r.author_kana, r.author_romaji) AGAINST (? IN NATURAL LANGUAGE MODE)
				    ) AS relevance_score
				FROM product p
				LEFT JOIN readings r ON p.id = r.product_id
				WHERE (
				    MATCH(p.title) AGAINST (? IN NATURAL LANGUAGE MODE) OR
				    MATCH(p.author) AGAINST (? IN NATURAL LANGUAGE MODE) OR
				    MATCH(p.genre) AGAINST (? IN NATURAL LANGUAGE MODE) OR
				    MATCH(p.notice) AGAINST (? IN NATURAL LANGUAGE MODE) OR
				    MATCH(r.title_hira, r.title_kana, r.title_romaji) AGAINST (? IN NATURAL LANGUAGE MODE) OR
				    MATCH(r.author_hira, r.author_kana, r.author_romaji) AGAINST (? IN NATURAL LANGUAGE MODE)
				)
				AND p.view = true
				ORDER BY relevance_score DESC;
					    """;

		List<Product> results = jdbcTemplate.query(fulltextSql, new BeanPropertyRowMapper<>(Product.class),
				keyword, keyword, keyword, keyword, // p.title ~ p.notice
				keyword, keyword, // r.title系, r.author系
				keyword, keyword, keyword, keyword, keyword, keyword // WHERE 条件用（6つ）
		);

		// 空なら fallback
		if (results.isEmpty()) {
			char[] chars = keyword.toCharArray();
			StringBuilder likeClause = new StringBuilder();
			StringBuilder scoreClause = new StringBuilder(" (");
			List<Object> params = new ArrayList<>();

			likeClause.append("SELECT DISTINCT p.*, ");
			likeClause.append("("); // relevance_score開始

			for (int i = 0; i < chars.length; i++) {
				String likeStr = "%" + chars[i] + "%";

				// フィールドごとにスコア加算
				if (i > 0) {
					likeClause.append(" + ");
					scoreClause.append(" + ");
				}

				likeClause.append(
						"(CASE WHEN p.title LIKE ? THEN 3 ELSE 0 END + " +
								"CASE WHEN p.author LIKE ? THEN 2 ELSE 0 END + " +
								"CASE WHEN p.genre LIKE ? THEN 1 ELSE 0 END + " +
								"CASE WHEN p.notice LIKE ? THEN 0.5 ELSE 0 END + " +
								"CASE WHEN r.title_hira LIKE ? THEN 3 ELSE 0 END + " +
								"CASE WHEN r.title_kana LIKE ? THEN 2 ELSE 0 END + " +
								"CASE WHEN r.title_romaji LIKE ? THEN 1 ELSE 0 END + " +
								"CASE WHEN r.author_hira LIKE ? THEN 2 ELSE 0 END + " +
								"CASE WHEN r.author_kana LIKE ? THEN 2 ELSE 0 END + " +
								"CASE WHEN r.author_romaji LIKE ? THEN 1 ELSE 0 END)");

				scoreClause.append(
						"(CASE WHEN p.title LIKE ? THEN 3 ELSE 0 END + " +
								"CASE WHEN p.author LIKE ? THEN 2 ELSE 0 END + " +
								"CASE WHEN p.genre LIKE ? THEN 1 ELSE 0 END + " +
								"CASE WHEN p.notice LIKE ? THEN 1 ELSE 0 END + " +
								"CASE WHEN r.title_hira LIKE ? THEN 3 ELSE 0 END + " +
								"CASE WHEN r.title_kana LIKE ? THEN 2 ELSE 0 END + " +
								"CASE WHEN r.title_romaji LIKE ? THEN 1 ELSE 0 END + " +
								"CASE WHEN r.author_hira LIKE ? THEN 3 ELSE 0 END + " +
								"CASE WHEN r.author_kana LIKE ? THEN 2 ELSE 0 END + " +
								"CASE WHEN r.author_romaji LIKE ? THEN 1 ELSE 0 END)");

				// 同じLIKEを各フィールドに適用
				for (int j = 0; j < 10; j++) {
					params.add(likeStr);
				}
			}

			likeClause.append(") AS relevance_score ");
			likeClause.append("FROM product p ");
			likeClause.append("LEFT JOIN readings r ON p.id = r.product_id ");
			likeClause.append("WHERE p.view = true ");
			likeClause.append("HAVING relevance_score > 0 ");
			likeClause.append("ORDER BY relevance_score DESC");

			return jdbcTemplate.query(likeClause.toString(),
					new BeanPropertyRowMapper<>(Product.class), params.toArray());
		}

		return results;
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