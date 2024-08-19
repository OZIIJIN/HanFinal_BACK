package org.onesentence.onesentence.domain.gpt.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.gpt.dto.GPTAnalyzeResponse;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoRequest;
import org.onesentence.onesentence.domain.gpt.service.GptService;
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
	public ResponseEntity<GPTCallTodoRequest> chat(@RequestParam("prompt") String prompt)
		throws JsonProcessingException {
		GPTCallTodoRequest response = gptService.gptCall(prompt);
		//String response = gptService.test(prompt);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/analyze")
	public ResponseEntity<GPTAnalyzeResponse> analyze(@RequestParam("todo") Long todoId, @RequestParam("prompt") String prompt)
		throws JsonProcessingException {
		GPTAnalyzeResponse response = gptService.gptCallForTodoCoordination(prompt, todoId);
		//String response = gptService.test(prompt);

		return ResponseEntity.ok().body(response);
	}
}
