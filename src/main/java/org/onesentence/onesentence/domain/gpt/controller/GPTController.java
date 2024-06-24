package org.onesentence.onesentence.domain.gpt.controller;

import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.gpt.dto.GPTRequest;
import org.onesentence.onesentence.domain.gpt.dto.GPTResponse;
import org.onesentence.onesentence.domain.gpt.service.GptService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatGpt")
public class GPTController {

	private final GptService gptService;

	@GetMapping("/chat")
	public ResponseEntity<String> chat(@RequestParam("prompt") String prompt){
		String response = gptService.gptCall(prompt);

		return ResponseEntity.ok().body(response);
	}
}
