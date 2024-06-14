package org.onesentence.onesentence.domain.todo.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.todo.dto.TodoRequest;
import org.onesentence.onesentence.domain.todo.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todos")
public class TodoController {

	private final TodoService todoService;

	@PostMapping
	public ResponseEntity<String> createTodo(@RequestBody TodoRequest request) {
		Long todoId = todoService.createTodo(request);

		return ResponseEntity.created(URI.create("/api/v1/todos/" + todoId)).build();
	}

}
