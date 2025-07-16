package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.Product;
import com.example.repository.SearchRepository;

@Service
public class SearchService {
	private final SearchRepository searchRepository;

	public SearchService(SearchRepository searchRepository) {
		this.searchRepository = searchRepository;
	}

	public List<Product> searchByKeyword(String keyword, String genre) {
		return searchRepository.searchByKeyword(keyword,genre);
	}

	public List<Product> findSearchAll(int id, String name, String author, String genre) {
		return searchRepository.findSearchAll(id, name, author, genre);
	}

}
