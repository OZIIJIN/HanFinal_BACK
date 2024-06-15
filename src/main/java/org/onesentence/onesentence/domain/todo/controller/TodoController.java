package org.onesentence.onesentence.domain.todo.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.onesentence.onesentence.domain.todo.dto.TodoRequest;
import org.onesentence.onesentence.domain.todo.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	@PutMapping
	public ResponseEntity<String> updateTodo(@RequestBody TodoRequest request, @PathVariable Long todoId) {
		Long updatedTodoId = todoService.updateTodo(request, todoId);

		return ResponseEntity.created(URI.create("/api/v1/todos/" + todoId)).build();
	}

}
