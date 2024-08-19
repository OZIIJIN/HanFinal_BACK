package org.onesentence.onesentence.domain.todo.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoDate {

	Long todoId;

	String title;

	LocalDateTime todoStart;

	LocalDateTime todoEnd;

	int inputTime;
}
