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

	public void setInputTime(int time) {
		this.inputTime = time;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}
}
