package com.example.service;

import org.springframework.stereotype.Service;

import com.example.model.Comment;
import com.example.repository.CommentRepository;

@Service
public class CommentService {
	private CommentRepository commentRepository;

	public CommentService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	public void addReview(int product_id, int user_id, String writeReview) {
		commentRepository.addReview(product_id, user_id, writeReview);
	}

	public Comment findAllReviewId(int product_id) {
		return commentRepository.findByReviewId(product_id);
	}

}
