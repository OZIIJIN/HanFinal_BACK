package org.onesentence.onesentence.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoordinationMessage {

	private String label;

	private Long todoId;

	private String message;

	private String todoTitle;

	private String start;

	private String end;

}
