package org.onesentence.onesentence.domain.todo.service;

import org.onesentence.onesentence.domain.todo.dto.TodoRequest;

public interface TodoService {

	Long createTodo(TodoRequest request);
}
