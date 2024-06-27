package org.onesentence.onesentence.domain.gpt.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GPTRequest {

	private String model;
	private List<Message> messages;
	private int temperature;
	private int maxTokens;
	private int topP;
	private int frequencyPenalty;
	private int presencePenalty;

	private static final String SYSTEM_MESSAGE =
		"assistant는 일정 관리 해주는 앱이야. user 의 prompt 문장을 일정으로 등록하려는데 JSON 형태로 일정 내용, 카테고리, 날짜로 분류해줘. 현재 날짜는 "
			+ LocalDateTime.now() + "이야. 양식: {\n"
			+ "  \"title\": \"치과\",\n"
			+ "  \"category\": \"의료\",\n"
			+ "  \"todoDate\": \"2024-06-27T20:00\"\n"
			+ "}";

	public GPTRequest(String model, String prompt, int temperature, int maxTokens, int topP,
		int frequencyPenalty, int presencePenalty) {
		this.model = model;
		this.messages = new ArrayList<>();
		this.messages.add(new Message("system", SYSTEM_MESSAGE));
		this.messages.add(new Message("user", prompt));
		this.temperature = temperature;
		this.maxTokens = maxTokens;
		this.topP = topP;
		this.frequencyPenalty = frequencyPenalty;
		this.presencePenalty = presencePenalty;
	}

}
