package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.Inquiry;
import com.example.model.User;
import com.example.repository.WMUserRepository;

@Service
public class WMUserService {

	private WMUserRepository wm;

	public WMUserService(WMUserRepository wm) {
		this.wm = wm;
	}

	public void updateInquiry() {
	}

	public void insertInquiry(String subject, String content) {
		wm.insertInquiry(subject, content);
	}

	public void updateInquiryAnswerd(Inquiry inquiry) {
		wm.updateInquiryAnswerd(inquiry);
	}

	public List<Inquiry> selectAnsweredInquiry() {

		return wm.selectAnsweredInquiry();
	}

	public List<Inquiry> selectNotAnsweredInquiry() {
		return wm.selectNotAnsweredInquiry();
	}

	public List<User> findByAllUsers() {
		return wm.findByAllUsers();
	}

}
