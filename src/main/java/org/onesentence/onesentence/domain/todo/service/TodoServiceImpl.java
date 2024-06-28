package org.onesentence.onesentence.domain.todo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.todo.dto.TodoRequest;
import org.onesentence.onesentence.domain.todo.dto.TodoResponse;
import org.onesentence.onesentence.domain.todo.dto.TodoStatusRequest;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;
import org.onesentence.onesentence.domain.todo.repository.TodoJpaRepository;
import org.onesentence.onesentence.global.exception.ExceptionStatus;
import org.onesentence.onesentence.global.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

	private final TodoJpaRepository todoJpaRepository;

	@Override
	@Transactional
	public Long createTodo(TodoRequest request) {

		Todo todo = new Todo(request);
		Todo savedTodo = todoJpaRepository.save(todo);

		return savedTodo.getId();
	}

	@Override
	@Transactional
	public Long updateTodo(TodoRequest request, Long todoId) {

		Todo todo = findById(todoId);
		todo.updateTodo(request);

		return todo.getId();
	}

	@Override
	public Todo findById(Long todoId) {
		return todoJpaRepository.findById(todoId).orElseThrow(() -> new NotFoundException(
			ExceptionStatus.NOT_FOUND));
	}

	@Override
	@Transactional
	public void deleteTodo(Long todoId) {
		Todo todo = findById(todoId);
		todoJpaRepository.delete(todo);
	}

	@Override
	@Transactional
	public Long updateStatus(TodoStatusRequest request, Long todoId) {
		Todo todo = findById(todoId);
		if (request.getStatus().equals("진행중")) {
			todo.changeToInProgress();
		} else if (request.getStatus().equals("완료")) {
			todo.changeToDone();
		}

		return todo.getId();
	}

	@Override
	@Transactional(readOnly = true)
	public TodoResponse getTodo(Long todoId) {
		Todo todo = findById(todoId);

		return TodoResponse.from(todo);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TodoResponse> getTodosByStatus(TodoStatus status) {
		List<TodoResponse> todoResponses = new ArrayList<>();

		List<Todo> todos = todoJpaRepository.findByStatus(status);

		for (Todo todo : todos) {
			todoResponses.add(TodoResponse.from(todo));
		}

		return todoResponses;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TodoResponse> getTodosByDate(LocalDate date) {
		List<TodoResponse> todoResponses = new ArrayList<>();
		LocalDateTime dayStart = date.atStartOfDay();
		LocalDateTime dayEnd = date.atTime(LocalTime.MAX);

		List<Todo> todos = todoJpaRepository.findByStartBetween(dayStart, dayEnd);

		for (Todo todo : todos) {
			todoResponses.add(TodoResponse.from(todo));
		}

		return todoResponses;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TodoResponse> getTodosByCategory(Long categoryId) {
		List<TodoResponse> todoResponses = new ArrayList<>();

		List<Todo> todos = todoJpaRepository.findByCategoryId(categoryId);

		for (Todo todo : todos) {
			todoResponses.add(TodoResponse.from(todo));
		}

		return todoResponses;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TodoResponse> getTodos() {
		List<TodoResponse> todoResponses = new ArrayList<>();

		List<Todo> todos = todoJpaRepository.findAll();

		for (Todo todo : todos) {
			todoResponses.add(TodoResponse.from(todo));
		}

		return todoResponses;
	}
}
