package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.ViewLog;
import com.example.repository.ViewLogRepository;

@Service
public class ViewLogService {

	private final ViewLogRepository viewLogRepository;

	public ViewLogService(ViewLogRepository viewLogRepository) {
		this.viewLogRepository = viewLogRepository;
	}

	//閲覧履歴保存
	public void addViewLog(String username, int product_id) {
		viewLogRepository.addViewLog(username, product_id);
	}
	
	//閲覧履歴取得
	public List<ViewLog> findAllViewLog(String username) {
		return viewLogRepository.findAllViewLog(username);
	}
}
