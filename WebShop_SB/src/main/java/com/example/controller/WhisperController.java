package com.example.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class WhisperController {
	
	//音声処理
	@PostMapping(path = "/api/speech/recognize", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public ResponseEntity<String> recognizeSpeech(@RequestPart("audioFile") MultipartFile audioFile) {
		try {
			HttpClient httpClient = HttpClient.newHttpClient();
			HttpRequest.BodyPublisher body = buildMultipartBody(audioFile);

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("http://localhost:5005/transcribe"))
					.header("Content-Type", "multipart/form-data; boundary=----JavaBoundary")
					.POST(body)
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			return ResponseEntity.ok(response.body());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("エラー: " + e.getMessage());
		}
	}

	private HttpRequest.BodyPublisher buildMultipartBody(MultipartFile file) throws IOException {
		String boundary = "----JavaBoundary";
		var byteArrays = new ArrayList<byte[]>();

		String header = "--" + boundary + "\r\n"
				+ "Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getOriginalFilename() + "\"\r\n"
				+ "Content-Type: " + file.getContentType() + "\r\n\r\n";

		byteArrays.add(header.getBytes(StandardCharsets.UTF_8));
		byteArrays.add(file.getBytes());
		byteArrays.add("\r\n--".concat(boundary).concat("--").getBytes(StandardCharsets.UTF_8));

		return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
	}

}
