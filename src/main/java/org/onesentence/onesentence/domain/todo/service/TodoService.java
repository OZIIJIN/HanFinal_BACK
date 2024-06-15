package org.onesentence.onesentence.domain.todo.service;

import org.onesentence.onesentence.domain.todo.dto.TodoRequest;
import org.onesentence.onesentence.domain.todo.entity.Todo;

public interface TodoService {

	Long createTodo(TodoRequest request);

	Long updateTodo(TodoRequest request, Long todoId);

	Todo findById(Long todoId);

	void deleteTodo(Long todoId);
}
