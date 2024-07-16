package org.onesentence.onesentence.domain.gpt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoRequest;
import org.onesentence.onesentence.domain.gpt.service.GptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatGpt")
public class GPTController {

	private final GptService gptService;

	@GetMapping("/chat")
	public ResponseEntity<GPTCallTodoRequest> chat(@RequestParam("prompt") String prompt)
		throws JsonProcessingException {
		GPTCallTodoRequest response = gptService.gptCall(prompt);
		//String response = gptService.test(prompt);

		return ResponseEntity.ok().body(response);
	}
}
