package org.onesentence.onesentence.domain.text.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.gpt.dto.GPTResponse;
import org.onesentence.onesentence.domain.text.dto.TextRequest;
import org.onesentence.onesentence.domain.text.service.TextService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/texts")
public class TextController {

	private final TextService textService;

	@PostMapping
	public ResponseEntity<String> createText(@RequestBody TextRequest request)
		throws JsonProcessingException {

		Long todoId = textService.createText(request);

		return ResponseEntity.created(URI.create("/api/v1/todos/" + todoId)).build();
	}

}
