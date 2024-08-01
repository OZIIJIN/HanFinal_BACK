package org.onesentence.onesentence.domain.fcm.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@Builder
public class FCMSendDto {
	private String token;

	private String title;

	private String body;

	public FCMSendDto(String token, String title, String body) {
		this.token = token;
		this.title = title;
		this.body = body;
	}

}
