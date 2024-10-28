package org.onesentence.onesentence.domain.chat.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoordinationMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private String label;

	private Long todoId;

	private String message;

	private String todoTitle;

	private String start;

	private String nickName;
}
