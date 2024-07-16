package org.onesentence.onesentence.domain.todo.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoRequest;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequest {

	private String title;

	private LocalDateTime start;

	private LocalDateTime end;

	private String category;

	private TodoStatus status;

	private String location;

	private String together;

	private Integer inputTime;
}
