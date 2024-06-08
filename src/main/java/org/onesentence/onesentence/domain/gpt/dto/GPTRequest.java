package org.onesentence.onesentence.domain.gpt.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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

	public GPTRequest(String model, String prompt, int temperature, int maxTokens, int topP, int frequencyPenalty, int presencePenalty) {
		this.model = model;
		this.messages = new ArrayList<>();
		this.messages.add(new Message("system", "assistant는 일정 관리 해주는 앱이야."));
		this.messages.add(new Message("user", prompt));
		this.messages.add(new Message("system", " 이 문장 일정 등록하려고 하는데 일정 내용, 카테고리로 JSON 형태로 분류 해줘. 예시: {\n"
			+ "  \"title\": \"테니스\",\n"
			+ "  \"category\": \"운동\",\n"
			+ "}"));
		this.temperature = temperature;
		this.maxTokens = maxTokens;
		this.topP=topP;
		this.frequencyPenalty=frequencyPenalty;
		this.presencePenalty = presencePenalty;
	}

}
