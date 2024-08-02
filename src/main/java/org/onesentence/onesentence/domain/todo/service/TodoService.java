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

	Long createTodo(TodoRequest request, Long userId);

	Long updateTodo(TodoRequest request, Long todoId, Long userId);

	void deleteTodo(Long todoId, Long userId);

	Long updateStatus(TodoStatusRequest request, Long todoId);

	TodoResponse getTodo(Long todoId);

	List<TodoResponse> getTodosByStatus(TodoStatus status, Long userId);

	List<TodoResponse> getTodosByDate(LocalDate date, Long userId);

	List<TodoResponse> getTodosByCategory(String category, Long userId);

	List<TodoResponse> getTodos(Long userId);

	List<TodoResponse> getPriorities(Long userId);

	Long createTodoByOneSentence(GPTCallTodoRequest gptCallTodoRequest, Long userId);

	Long setInputTime(Long todoId, TodoInputTimeRequest request, Long userId);
}
