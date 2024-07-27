package org.onesentence.onesentence.domain.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoPriority {

	Long todoId;

	Double priorityScore;
}
