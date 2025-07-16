package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.SearchLog;
import com.example.repository.SearchLogRepository;

@Service
public class SearchLogService {
	
	private SearchLogRepository searchLogRepository;
	
	public SearchLogService(SearchLogRepository searchLogRepository) {
		this.searchLogRepository = searchLogRepository;
	}
	
	//検索履歴追加
	public void saveSearchLog(SearchLog searchLog) {
		searchLogRepository.saveSearchLog(searchLog);
	}

	public List<SearchLog> findSearchIdLog(String username, String place) {
		return searchLogRepository.findSearchIdLog(username, place);
	}

	public List<SearchLog> findSearchTitleLog(String username, String place) {
		return searchLogRepository.findSearchTitleLog(username, place);
	}

	public List<SearchLog> findSearchAuthorLog(String username, String place) {
		return searchLogRepository.findSearchAuthorLog(username, place);
	}

	public List<SearchLog> findSearchGenreLog(String username, String place) {
		return searchLogRepository.findSearchGenreLog(username, place);
	}
}
