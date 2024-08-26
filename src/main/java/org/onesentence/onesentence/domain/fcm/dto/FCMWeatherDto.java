package org.onesentence.onesentence.domain.fcm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class FCMWeatherDto {

	private String token;

	private String title;

	private String body;

	private Long todoId;

	private String date;

	private String type;

	public FCMWeatherDto(String token, String title, String body, Long todoId, String date, String type) {
		this.token = token;
		this.title = title;
		this.body = body;
		this.todoId = todoId;
		this.date = date;
		this.type = type;
	}

}
