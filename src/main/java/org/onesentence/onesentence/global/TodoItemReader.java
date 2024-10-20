package org.onesentence.onesentence.global;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.repository.TodoJpaRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class TodoItemReader implements ItemReader<Todo> {

	private final TodoJpaRepository todoJpaRepository;
	private int nextIndex;
	private List<Todo> todoList;

	@Autowired
	public TodoItemReader(TodoJpaRepository todoJpaRepository) {
		this.todoJpaRepository = todoJpaRepository;
		this.nextIndex = 0;
	}

	@Override
	public Todo read() {
		if (todoList == null) {
			LocalDateTime tomorrowStart = LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay();
			LocalDateTime tomorrowEnd = tomorrowStart.plusDays(1).minusSeconds(1);
			this.todoList = todoJpaRepository.findByStartBetween(tomorrowStart, tomorrowEnd);
		}

		Todo nextTodo = null;

		if (nextIndex < todoList.size()) {
			nextTodo = todoList.get(nextIndex);
			nextIndex++;
		}

		return nextTodo;
	}
}
