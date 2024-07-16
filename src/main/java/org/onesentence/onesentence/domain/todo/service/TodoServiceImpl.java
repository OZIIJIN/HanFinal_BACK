package org.onesentence.onesentence.domain.todo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoRequest;
import org.onesentence.onesentence.domain.todo.dto.*;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;
import org.onesentence.onesentence.domain.todo.repository.TodoJpaRepository;
import org.onesentence.onesentence.domain.todo.repository.TodoQuery;
import org.onesentence.onesentence.domain.todo.repository.TodoQueryImpl;
import org.onesentence.onesentence.global.exception.ExceptionStatus;
import org.onesentence.onesentence.global.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

	private final TodoJpaRepository todoJpaRepository;
	private final TodoQuery todoQuery;

	@Override
	@Transactional
	public Long createTodo(TodoRequest request) {

		Todo todo = new Todo(
			request.getTitle(),
			request.getStart(),
			request.getCategory(),
			request.getStatus(),
			request.getEnd(),
			request.getLocation(),
			request.getTogether(),
			request.getInputTime());
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
	public List<TodoResponse> getTodosByCategory(String category) {
		List<TodoResponse> todoResponses = new ArrayList<>();

		List<Todo> todos = todoJpaRepository.findByCategory(category);

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

	@Override
	public List<TodoPriority> getPriorities() {
		return todoQuery.calculatePriority();
	}

	@Override
	@Transactional
	public Long createTodoByOneSentence(GPTCallTodoRequest gptCallTodoRequest) {

		Todo todo = Todo.builder()
			.title(gptCallTodoRequest.getTitle())
			.start(gptCallTodoRequest.getStart())
			.category(gptCallTodoRequest.getCategory())
			.status(TodoStatus.TODO)
			.end(gptCallTodoRequest.getEnd())
			.location(gptCallTodoRequest.getLocation())
			.together(gptCallTodoRequest.getTogether())
			.build();
		Todo savedTodo = todoJpaRepository.save(todo);

		return savedTodo.getId();
	}

	@Override
	@Transactional
	public Long setInputTime(Long todoId, TodoInputTimeRequest request) {

		Todo todo = findById(todoId);
		todo.setInputTime(request.getInputTime());

		return todo.getId();
	}
}
