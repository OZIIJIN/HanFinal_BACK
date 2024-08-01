package org.onesentence.onesentence.domain.todo.service;

import java.time.LocalDate;
import java.util.List;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoRequest;
import org.onesentence.onesentence.domain.todo.dto.TodoInputTimeRequest;
import org.onesentence.onesentence.domain.todo.dto.TodoRequest;
import org.onesentence.onesentence.domain.todo.dto.TodoResponse;
import org.onesentence.onesentence.domain.todo.dto.TodoStatusRequest;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;

public interface TodoService {

	Long createTodo(TodoRequest request);

	Long updateTodo(TodoRequest request, Long todoId);

	void deleteTodo(Long todoId);

	Long updateStatus(TodoStatusRequest request, Long todoId);

	TodoResponse getTodo(Long todoId);

	List<TodoResponse> getTodosByStatus(TodoStatus status);

	List<TodoResponse> getTodosByDate(LocalDate date);

	List<TodoResponse> getTodosByCategory(String category);

	List<TodoResponse> getTodos();

	List<TodoResponse> getPriorities();

	Long createTodoByOneSentence(GPTCallTodoRequest gptCallTodoRequest);

	Long setInputTime(Long todoId, TodoInputTimeRequest request);
}
