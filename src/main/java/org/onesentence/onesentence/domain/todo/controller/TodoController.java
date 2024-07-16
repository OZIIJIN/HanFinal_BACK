package org.onesentence.onesentence.domain.todo.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.todo.dto.*;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;
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

	@PutMapping("/{todoId}")
	public ResponseEntity<String> updateTodo(@RequestBody TodoRequest request,
		@PathVariable Long todoId) {
		Long updatedTodoId = todoService.updateTodo(request, todoId);

		return ResponseEntity.created(URI.create("/api/v1/todos/" + todoId)).build();
	}

	@DeleteMapping("/{todoId}")
	public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId) {
		todoService.deleteTodo(todoId);

		return ResponseEntity.ok().build();
	}

	@PatchMapping("/{todoId}")
	public ResponseEntity<String> setInputTime(@PathVariable Long todoId,
		@RequestBody TodoInputTimeRequest request) {
		Long updatedTodoId = todoService.setInputTime(todoId, request);

		return ResponseEntity.created(URI.create("/api/v1/todos/" + todoId)).build();
	}

	@PatchMapping("/{todoId}/status-updates")
	public ResponseEntity<String> updateStatus(@RequestBody TodoStatusRequest request,
		@PathVariable Long todoId) {
		Long updatedTodoId = todoService.updateStatus(request, todoId);

		return ResponseEntity.created(URI.create("/api/v1/todos/" + todoId)).build();
	}

	@GetMapping("/{todoId}")
	public ResponseEntity<TodoResponse> getTodo(@PathVariable Long todoId) {
		TodoResponse response = todoService.getTodo(todoId);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/statuses")
	public ResponseEntity<List<TodoResponse>> getTodosByStatus(@RequestParam TodoStatus status) {
		List<TodoResponse> todoResponses = todoService.getTodosByStatus(status);

		return ResponseEntity.ok().body(todoResponses);
	}

	@GetMapping("/dates")
	public ResponseEntity<List<TodoResponse>> getTodosByDate(@RequestParam LocalDate date) {
		List<TodoResponse> todoResponses = todoService.getTodosByDate(date);

		return ResponseEntity.ok().body(todoResponses);
	}

	@GetMapping("/categories/{category}")
	public ResponseEntity<List<TodoResponse>> getTodosByCategory(@PathVariable String category) {
		List<TodoResponse> todoResponses = todoService.getTodosByCategory(category);

		return ResponseEntity.ok().body(todoResponses);
	}

	@GetMapping()
	public ResponseEntity<List<TodoResponse>> getTodos() {
		List<TodoResponse> todoResponses = todoService.getTodos();

		return ResponseEntity.ok().body(todoResponses);
	}

	@GetMapping("/priorities")
	public ResponseEntity<List<TodoPriority>> getPriorities() {
		List<TodoPriority> priorities = todoService.getPriorities();

		return ResponseEntity.ok().body(priorities);
	}
}
