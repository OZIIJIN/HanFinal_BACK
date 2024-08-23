package org.onesentence.onesentence.domain.todo.dto;

import java.time.LocalDateTime;
import kotlin.collections.DoubleIterator;
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

	private LocalDateTime end;

	private String category;

	private TodoStatus status;

	private String location;

	private String together;

	private int inputTime;

	public static TodoResponse from(Todo todo) {
		return new TodoResponse(
			todo.getId(),
			todo.getTitle(),
			todo.getStart(),
			todo.getEnd(),
			todo.getCategory(),
			todo.getStatus(),
			todo.getLocation(),
			todo.getTogether(),
			todo.getInputTime()
		);
	}
}
