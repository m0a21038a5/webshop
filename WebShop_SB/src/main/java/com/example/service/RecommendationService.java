package com.example.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.BuyLog;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.repository.WMProRepository;

@Service
public class RecommendationService {
	
	@Autowired
	private WMProRepository buyLogRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	public Optional<Product> recommendProduct(List<Product> cart, String username) {
        List<BuyLog> logs = buyLogRepository.findByAllBuylog();
        List<Product> allProducts = productRepository.findViewAll();
        List<List<Integer>> userVectors = buildUserVectors(logs, allProducts);
        List<List<Double>> similarityMatrix = calculateCosineSimilarity(userVectors);

        List<Double> cartVector = toCartVector(cart, allProducts);
        int bestProductIndex = findBestRecommendation(similarityMatrix, cartVector, allProducts);

        if (bestProductIndex != -1) {
            return Optional.of(allProducts.get(bestProductIndex));
        }
        return Optional.empty();
    }

    // 各ユーザの購入量ベクトルを構築
    private List<List<Integer>> buildUserVectors(List<BuyLog> logs, List<Product> allProducts) {
    	Map<String, List<Integer>> userVectors = new HashMap<>();

        for (BuyLog log : logs) {
            String username = log.getUsername();
            int productIndex = indexOfProduct(log.getProductid(), allProducts);
            if (productIndex == -1) continue;

            userVectors.putIfAbsent(username, new ArrayList<>(Collections.nCopies(allProducts.size(), 0)));
            List<Integer> vector = userVectors.get(username);
            vector.set(productIndex, vector.get(productIndex) + log.getQuantity());
        }

        return new ArrayList<>(userVectors.values());
    }

    private int indexOfProduct(int productId, List<Product> allProducts) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == productId) return i;
        }
        return -1;
    }

    // コサイン類似度を行列形式で計算
    private List<List<Double>> calculateCosineSimilarity(List<List<Integer>> vectors) {
    	 int size = vectors.get(0).size(); // 商品数
    	    List<List<Double>> cosineMatrix = new ArrayList<>();

    	    for (int i = 0; i < size; i++) {
    	        List<Double> row = new ArrayList<>();
    	        for (int j = 0; j < size; j++) {
    	            double dot = 0, normA = 0, normB = 0;
    	            for (List<Integer> vector : vectors) {
    	                dot += vector.get(i) * vector.get(j);
    	                normA += Math.pow(vector.get(i), 2);
    	                normB += Math.pow(vector.get(j), 2);
    	            }
    	            double cos = (normA == 0 || normB == 0) ? 0.0 : dot / (Math.sqrt(normA) * Math.sqrt(normB));
    	            row.add(cos);
    	        }
    	        cosineMatrix.add(row);
    	    }

    	    return cosineMatrix;
    }

    // カートベクトル生成
    private List<Double> toCartVector(List<Product> cart, List<Product> allProducts) {
    	List<Double> vector = new ArrayList<>(Collections.nCopies(allProducts.size(), 0.0));

        for (Product p : cart) {
            int index = indexOfProduct(p.getId(), allProducts);
            if (index != -1) {
                vector.set(index, (double) p.getQuantity());
            }
        }

        return vector;
    }

    // 一番似ている商品を選ぶ
    private int findBestRecommendation(List<List<Double>> cosSim, List<Double> cartVec, List<Product> all) {
    	List<Integer> candidates = new ArrayList<>();

        for (int i = 0; i < cartVec.size(); i++) {
            if (cartVec.get(i) == 0.0) {
                candidates.add(i);
            }
        }

        if (candidates.isEmpty()) return -1;

        double bestScore = -1.0;
        int bestIndex = -1;

        for (int i : candidates) {
            // 最大類似度2つを加重平均
            double first = 0, second = 0;
            int firstIdx = -1, secondIdx = -1;

            for (int j = 0; j < cartVec.size(); j++) {
                double sim = cosSim.get(i).get(j);
                if (sim > first && !Double.valueOf(sim).equals(1.0)) {
                    second = first;
                    secondIdx = firstIdx;
                    first = sim;
                    firstIdx = j;
                } else if (sim > second && sim != first && !Double.valueOf(sim).equals(1.0)) {
                    second = sim;
                    secondIdx = j;
                }
            }

            double weightSum = first + second;
            if (weightSum == 0) continue;

            double score = (cartVec.get(firstIdx) * first + cartVec.get(secondIdx) * second) / weightSum;
            if (score > bestScore) {
                bestScore = score;
                bestIndex = i;
            }
        }

        return bestIndex;
    }
}
