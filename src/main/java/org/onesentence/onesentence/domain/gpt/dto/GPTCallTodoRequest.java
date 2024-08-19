package org.onesentence.onesentence.domain.gpt.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GPTCallTodoRequest {

	private String title;

	private LocalDateTime start;

	private LocalDateTime end;

	private String category;

	private String location;

	private String together;

	private Integer inputTime;
}
