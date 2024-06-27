package org.onesentence.onesentence.domain.todo.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.onesentence.onesentence.domain.todo.entity.Path;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;

@Getter
@AllArgsConstructor
public class TodoResponse {

	private Long todoId;

	private String title;

	private LocalDateTime start;

	private Path path;

	private LocalDateTime end;

	private Long categoryId;

	private TodoStatus status;

	public static TodoResponse from(Todo todo) {
		return new TodoResponse(
			todo.getId(),
			todo.getTitle(),
			todo.getStart(),
			todo.getPath(),
			todo.getEnd(),
			todo.getCategoryId(),
			todo.getStatus()
		);
	}
}
