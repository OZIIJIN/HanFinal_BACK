package org.onesentence.onesentence.domain.gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoResponse;
import org.onesentence.onesentence.domain.gpt.dto.GPTRequest;
import org.onesentence.onesentence.domain.gpt.dto.GPTResponse;
import org.onesentence.onesentence.domain.gpt.dto.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GptServiceImpl implements GptService {

	@Value("${openai.model:gpt-3.5-turbo}")
	private String model;

	@Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
	private String apiUrl;

	private final String SYSTEM_MESSAGE =
		"너는 한 문장에서 여러 단어를 분류하는 역할이야. prompt 문장을 일정으로 등록하려는데 JSON 형태로 일정 내용, 카테고리, 일정 시작 시간, 일정 끝 시간, 장소, 협업자로 분류해줘. 해당 값이 없으면 null 표시해줘. 아래 양식 꼭 지켜줘. 현재 날짜는 "
			+ LocalDateTime.now()
			+ "이야. title, category, location, together 는 String 타입, start, end 는 LocalDateTime 타입이야.";

	private final RestTemplate restTemplate;

	private final ObjectMapper objectMapper;

	@Override
	public GPTCallTodoResponse gptCall(String prompt) throws JsonProcessingException {
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", SYSTEM_MESSAGE));
		messages.add(new Message("user", prompt));

		GPTRequest request = new GPTRequest(
			model, prompt, 0, 256, 1, 0, 0, messages);

		GPTResponse gptResponse = restTemplate.postForObject(
			apiUrl
			, request
			, GPTResponse.class
		);

		String jsonString = gptResponse.getChoices().get(0).getMessage().getContent();

		return objectMapper.readValue(jsonString,
			GPTCallTodoResponse.class);
	}
}
