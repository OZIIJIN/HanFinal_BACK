package org.onesentence.onesentence.domain.text.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoResponse;
import org.onesentence.onesentence.domain.gpt.service.GptServiceImpl;
import org.onesentence.onesentence.domain.text.dto.TextRequest;
import org.onesentence.onesentence.domain.todo.dto.TodoRequest;
import org.onesentence.onesentence.domain.todo.service.TodoServiceImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TextServiceImpl implements TextService {

	private final RestTemplate restTemplate;
	private final GptServiceImpl gptService;
	private final TodoServiceImpl todoService;

	@Override
	public Long createText(TextRequest request) throws JsonProcessingException {
		String text = request.getText();

		GPTCallTodoResponse gptCallResponse = gptService.gptCall(text);

		TodoRequest todoRequest = TodoRequest.gptResponseToRequest(gptCallResponse);

		return todoService.createTodo(todoRequest);
	}
}
