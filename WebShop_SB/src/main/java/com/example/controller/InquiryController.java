package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.Inquiry;
import com.example.service.InquiryService;

@Controller
public class InquiryController {

	private InquiryService inquiryService;

	public InquiryController(InquiryService inquiryService) {
		this.inquiryService = inquiryService;
	}

	//お問い合わせ
	@GetMapping("/inquiry")
	public String inquiry() {
		return "inquiry";
	}

	@PostMapping("/inquiry")
	public String inquiryform(@RequestParam("subject") String subject, @RequestParam("content") String content) {

		inquiryService.insertInquiry(subject, content);

		return "redirect:/inquiry";

	}

	//お問い合わせの返答
	@GetMapping("/answer")
	public String answer(Model model) {
		List<Inquiry> list = new ArrayList<>();

		list = inquiryService.selectNotAnsweredInquiry();

		model.addAttribute("inquiry", list);

		return "Answer";
	}

	@PostMapping("/answer")
	public String answerform(@RequestParam("answercontent") String answercontent,
			@RequestParam("inquiryid") int inquiryid) {
		List<Inquiry> list = new ArrayList<>();

		list = inquiryService.selectNotAnsweredInquiry();
		for (Inquiry inquiry : list) {
			if (inquiry.getId() == inquiryid) {
				inquiry.setAnswercontent(answercontent);
				inquiryService.updateInquiryAnswerd(inquiry);
			}
		}

		return "redirect:/answercomplete";

	}

	//お問い合わせ返答完了ページ
	@GetMapping("/answercomplete")
	public String answercomplete() {
		return "AnswerComplete";
	}

	//Q&A
	@GetMapping("/qa")
	public String qa(Model model) {
		List<Inquiry> list = new ArrayList<>();
		list = inquiryService.selectAnsweredInquiry();

		model.addAttribute("answeredinquiry", list);

		return "QA";
	}

}
