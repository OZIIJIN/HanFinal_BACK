package org.onesentence.onesentence.domain.gpt.controller;

import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.gpt.dto.GPTRequest;
import org.onesentence.onesentence.domain.gpt.dto.GPTResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatGpt")
public class GPTController {

	@Value("${openai.model:gpt-3.5-turbo}")
	private String model;

	@Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
	private String apiUrl;

	private final RestTemplate restTemplate;

	@GetMapping("/chat")
	public String chat(@RequestParam("prompt") String prompt){

		GPTRequest request = new GPTRequest(
			model,prompt,1,256,1,2,2);

		GPTResponse gptResponse = restTemplate.postForObject(
			apiUrl
			, request
			, GPTResponse.class
		);


		return gptResponse.getChoices().get(0).getMessage().getContent();


	}
}
