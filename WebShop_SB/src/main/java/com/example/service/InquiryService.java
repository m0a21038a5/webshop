package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.Inquiry;
import com.example.model.User;
import com.example.repository.InquiryRepository;

@Service
public class InquiryService {
	private InquiryRepository inquiryRepository;

	public InquiryService(InquiryRepository inquiryRepository) {
		this.inquiryRepository = inquiryRepository;
	}
	
	public void insertInquiry(String subject, String content) {
		inquiryRepository.insertInquiry(subject, content);
	}

	public void updateInquiryAnswerd(Inquiry inquiry) {
		inquiryRepository.updateInquiryAnswerd(inquiry);
	}

	public List<Inquiry> selectAnsweredInquiry() {

		return inquiryRepository.selectAnsweredInquiry();
	}

	public List<Inquiry> selectNotAnsweredInquiry() {
		return inquiryRepository.selectNotAnsweredInquiry();
	}

	public List<User> findByAllUsers() {
		return inquiryRepository.findByAllUsers();
	}
}
