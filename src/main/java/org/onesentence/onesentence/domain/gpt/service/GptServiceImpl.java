package org.onesentence.onesentence.domain.gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoRequest;
import org.onesentence.onesentence.domain.gpt.dto.GPTRequest;
import org.onesentence.onesentence.domain.gpt.dto.GPTResponse;
import org.onesentence.onesentence.domain.gpt.dto.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GptServiceImpl implements GptService {

	// GPT-3.5 API 호출에 사용할 모델을 지정, 기본값은 'gpt-3.5-turbo'
	@Value("${openai.model:gpt-3.5-turbo}")
	private String model;

	// OpenAI API의 URL을 지정, 기본값은 채팅 컴플리션 엔드포인트
	@Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
	private String apiUrl;

	// 시스템 메시지를 정의, 현재 날짜와 시간 포함
	private final String SYSTEM_MESSAGE =
		"너는 한 문장에서 여러 단어를 분류하는 역할이야. "
			+ "prompt 문장을 일정으로 등록하려는데 JSON 형태로 일정 내용, 카테고리, 일정 시작 시간, 일정 끝 시간, 장소, 협업자로 분류해줘. 해당 값이 없으면 null 표시해줘. "
			+ "아래 양식 꼭 지켜줘. 현재 날짜는 "
			+ LocalDateTime.now()
			+ "이야. title, category, location, together 는 String 타입, start, end 는 LocalDateTime 타입이야.";

	// RestTemplate 객체를 주입받음
	private final RestTemplate restTemplate;

	// ObjectMapper 객체를 주입받음
	private final ObjectMapper objectMapper;

	@Override
	public GPTCallTodoRequest gptCall(String prompt) throws JsonProcessingException {
		// 메시지 리스트를 생성하고 시스템 메시지와 사용자 메시지를 추가
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", SYSTEM_MESSAGE));
		messages.add(new Message("user", prompt));

		// GPT 요청 객체를 생성, 모델명, 파라미터 등 설정
		GPTRequest request = new GPTRequest(
			model, 0, 256, 1, 0, 0, messages);

		// OpenAI API에 POST 요청을 보내고 응답을 GPTResponse 객체로 받음
		GPTResponse gptResponse = restTemplate.postForObject(
			apiUrl
			, request
			, GPTResponse.class
		);

		// GPT 응답에서 첫 번째 선택지의 메시지 내용을 JSON 문자열로 가져옴
		String jsonString = gptResponse.getChoices().get(0).getMessage().getContent();

		// JSON 문자열을 GPTCallTodoRequest 객체로 변환하여 반환
		return objectMapper.readValue(jsonString,
			GPTCallTodoRequest.class);
	}

	@Override
	public String test(String prompt) {
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", SYSTEM_MESSAGE));
		messages.add(new Message("user", prompt));

		GPTRequest request = new GPTRequest(
			model,0, 256, 1, 0, 0, messages);

		GPTResponse gptResponse = restTemplate.postForObject(
			apiUrl
			, request
			, GPTResponse.class
		);

		String jsonString = gptResponse.getChoices().get(0).getMessage().getContent();

		//json 만 가져오게끔 코드 짜오기

		return jsonString;
	}
}
