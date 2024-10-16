package org.onesentence.onesentence.domain.fcm.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@Builder(toBuilder = true)
public class FCMSendDto {
	private String token;

	private String title;

	private String body;

	private Long todoId;

	public FCMSendDto(String token, String title, String body, Long todoId) {
		this.token = token;
		this.title = title;
		this.body = body;
		this.todoId = todoId;
	}


}
