package org.onesentence.onesentence.domain.gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.annotation.Timed;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.gpt.dto.*;
import org.onesentence.onesentence.domain.todo.dto.TodoDate;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GptServiceImpl implements GptService {

	@Value("${openai.model:gpt-4o}")
	private String model;

	@Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
	private String apiUrl;

	private final RestTemplate restTemplate;

	private final ObjectMapper objectMapper;

	private static final Logger gptLogger = LoggerFactory.getLogger("gpt");


	@Override
	@Timed(value = "gpt.api.request.time", description = "GPT API 요청")
	public GPTCallTodoRequest gptCall(String prompt) throws JsonProcessingException {
		String SYSTEM_MESSAGE =
			"너는 한 문장에서 여러 단어를 분류하는 역할이야. "
				+ "prompt 문장을 일정으로 등록하려는데 JSON 형태로 일정 내용, 카테고리, 일정 시작 시간, 일정 끝 시간, 장소, 협업자, 소요 시간(시간)으로 분류해줘. 해당 값이 없으면 null 표시해줘."
				+ "아래 양식 꼭 지켜줘. 현재 날짜는 "
				+ LocalDateTime.now(ZoneId.of("Asia/Seoul"))
				+ "이야. title, category, location, together 는 String 타입, start, end 는 LocalDateTime 타입이고 inputTime 은 Integer 타입이야.";

		long startTime = System.currentTimeMillis();

		List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", SYSTEM_MESSAGE));
		messages.add(new Message("user", prompt));

		GPTRequest request = new GPTRequest(
			model, 0, 256, 1, 0, 0, messages);

		GPTResponse gptResponse = restTemplate.postForObject(
			apiUrl
			, request
			, GPTResponse.class
		);

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;

		gptLogger.info("System Message: {}", SYSTEM_MESSAGE);
		gptLogger.info("GPT API 요청 소요 시간: {} ms", duration);

		String rawJsonString = gptResponse.getChoices().get(0).getMessage().getContent();

		String jsonString = rawJsonString
			.replace("```json", "")  // 시작 태그 제거
			.replace("```", "")      // 끝 태그 제거
			.trim();                 // 앞뒤 공백 제거

		return objectMapper.readValue(jsonString,
			GPTCallTodoRequest.class);
	}

	public GPTAnalyzeResponse gptCallForTodoCoordination(String prompt, Long todoId) throws JsonProcessingException {
		String SYSTEM_MESSAGE_ANALYZE =
			"너는 prompt에 담긴 문장이 긍정이면 yes, 부정이면 no로 분석하는 역할이야."
				+ "게다가 만약 prompt에 일정에 관련된 말이 포함되면 해당 일정을 LocalDateTime 타입으로 담아주고 일정에 관련된 말이 포함되면 no야."
				+ "JSON 형태로 analyze, date에 담아줘 만약 date가 없다고 판단하면 null 표시해줘. "
				+ "현재 날짜: " + LocalDateTime.now();

		List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", SYSTEM_MESSAGE_ANALYZE));
		messages.add(new Message("user", prompt));

		GPTRequest request = new GPTRequest(
			model, 0, 256, 1, 0, 0, messages);

		GPTResponse gptResponse = restTemplate.postForObject(
			apiUrl
			, request
			, GPTResponse.class
		);

		String rawJsonString = gptResponse.getChoices().get(0).getMessage().getContent();

		String jsonString = rawJsonString
			.replace("```json", "")  // 시작 태그 제거
			.replace("```", "")      // 끝 태그 제거
			.trim();                 // 앞뒤 공백 제거

		GPTAnalyzeResponse gptAnalyzeResponse = objectMapper.readValue(jsonString, GPTAnalyzeResponse.class);

		return gptAnalyzeResponse;
	}
}
