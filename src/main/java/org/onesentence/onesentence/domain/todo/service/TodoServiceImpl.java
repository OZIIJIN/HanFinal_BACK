package org.onesentence.onesentence.domain.todo.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.todo.dto.TodoRequest;
import org.onesentence.onesentence.domain.todo.entity.Todo;
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
}
