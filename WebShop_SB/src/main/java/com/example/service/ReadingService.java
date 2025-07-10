package com.example.service;

import org.springframework.stereotype.Service;

import com.example.model.Readings;
import com.example.repository.ReadingRepository;

@Service
public class ReadingService {

	private ReadingRepository readingRepository;

	public ReadingService(ReadingRepository readingRepository) {
		this.readingRepository = readingRepository;
	}

	public Readings findByProductId(int product_id) {
		return readingRepository.findByProductId(product_id);
	}

	public void updateReading(int productId, String titleHira, String titleKana, String titleRomaji,
			String authorHira, String authorKana, String authorRomaji) {
		readingRepository.updateReading(productId, titleHira, titleKana, titleRomaji, authorHira, authorKana,
				authorRomaji);
	}
}
