package org.onesentence.onesentence.domain.text.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.gpt.dto.GPTResponse;
import org.onesentence.onesentence.domain.text.dto.TextRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/texts")
public class TextController {

	private final RestTemplate restTemplate;

	@PostMapping
	public ResponseEntity<String> createText(@RequestBody TextRequest request) {
		String text = request.getText();

		String url = "http://localhost:8080/api/v1/chatGpt/chat?prompt=" + text;

		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		return ResponseEntity.ok().body(response.getBody());
	}

}
