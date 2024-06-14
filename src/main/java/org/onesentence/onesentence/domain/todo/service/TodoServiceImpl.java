package org.onesentence.onesentence.domain.todo.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.todo.dto.TodoRequest;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.repository.TodoJpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

	private final TodoJpaRepository todoJpaRepository;

	@Override
	public Long createTodo(TodoRequest request) {

		Todo todo = new Todo(request);
		Todo savedTodo = todoJpaRepository.save(todo);

		return savedTodo.getId();
	}
}
