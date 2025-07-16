package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.Comment;
import com.example.model.alert_review;
import com.example.repository.CommentRepository;
import com.example.repository.UserAlertRepository;

@Service
public class CommentService {
	private final CommentRepository commentRepository;
	private final UserAlertRepository userAlertRepository;

	public CommentService(CommentRepository commentRepository, UserAlertRepository userAlertRepository) {
		this.commentRepository = commentRepository;
		this.userAlertRepository = userAlertRepository;
	}

	//レビュー追加
	public void addReview(int product_id, String username, String writeReview) {
		commentRepository.addReview(product_id, username, writeReview);
	}

	//レビュー全取得
	public List<Comment> findAllReviewId(int product_id) {
		return commentRepository.findByReviewId(product_id);
	}

	//通報済みユーザー判別
	public List<Comment> TakeReview(int product_id, String user_send_alert) {
		List<Comment> takeReview = commentRepository.findByReviewId(product_id);

		if (takeReview != null) {
			List<alert_review> AlertReview = userAlertRepository.UserName_AlertReviewRepo(user_send_alert);

			List<String> TakeUserAlertName = new ArrayList<>();
			for (alert_review alertRe : AlertReview) {
				String alna = alertRe.getUser_alert();
				TakeUserAlertName.add(alna);
			}

			for (Comment AlRecome : takeReview) {
				String alre = AlRecome.getUser_name();
				if (TakeUserAlertName.contains(alre)) {
					AlRecome.setAlreadyAlert(true);
				}
			}
		}
		return takeReview;
	}
}
