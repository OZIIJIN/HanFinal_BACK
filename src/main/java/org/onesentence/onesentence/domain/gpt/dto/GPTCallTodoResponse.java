package org.onesentence.onesentence.domain.gpt.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GPTCallTodoResponse {

	private String title;

	private LocalDateTime start;

	private LocalDateTime end;

	private String category;

	private String location;

	private String together;
}
