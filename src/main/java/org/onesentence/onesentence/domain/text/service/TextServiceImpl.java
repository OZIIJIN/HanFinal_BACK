package org.onesentence.onesentence.domain.text.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoRequest;
import org.onesentence.onesentence.domain.gpt.service.GptServiceImpl;
import org.onesentence.onesentence.domain.text.dto.TextRequest;
import org.onesentence.onesentence.domain.todo.dto.TodoRequest;
import org.onesentence.onesentence.domain.todo.service.TodoServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TextServiceImpl implements TextService {

	private final RestTemplate restTemplate;
	private final GptServiceImpl gptService;
	private final TodoServiceImpl todoService;

	@Override
	public Long createTodoByOneSentence(TextRequest request, Long userId)
		throws IOException, FirebaseMessagingException {
		String text = request.getText();

		GPTCallTodoRequest gptCallTodoRequest = gptService.gptCall(text);

		return todoService.createTodoByOneSentence(gptCallTodoRequest, userId);
	}

}
