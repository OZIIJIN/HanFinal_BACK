package org.onesentence.onesentence.domain.todo.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.todo.dto.*;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;
import org.onesentence.onesentence.domain.todo.service.TodoService;
import org.quartz.SchedulerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todos")
public class TodoController {

	private final TodoService todoService;

	@PostMapping
	public ResponseEntity<String> createTodo(@RequestBody TodoRequest request,
		@RequestAttribute("userId") Long userId) throws SchedulerException {
		Long todoId = todoService.createTodo(request, userId);

		return ResponseEntity.created(URI.create("/api/v1/todos/" + todoId)).build();
	}

	@GetMapping("/{todoId}/coordination")
	public ResponseEntity<Void> coordinateTodo(@RequestAttribute("userId") Long userId,
		@PathVariable Long todoId) throws SchedulerException {

		todoService.coordinateTodo(todoId, userId);

		return ResponseEntity.ok().build();
	}

	@PutMapping("/{todoId}")
	public ResponseEntity<String> updateTodo(@RequestBody TodoRequest request,
		@PathVariable Long todoId, @RequestAttribute("userId") Long userId) {
		Long updatedTodoId = todoService.updateTodo(request, todoId, userId);

		return ResponseEntity.created(URI.create("/api/v1/todos/" + todoId)).build();
	}

	@DeleteMapping("/{todoId}")
	public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId,
		@RequestAttribute("userId") Long userId) {

		todoService.deleteTodo(todoId, userId);

		return ResponseEntity.ok().build();
	}

	@PatchMapping("/{todoId}")
	public ResponseEntity<String> setInputTime(@PathVariable Long todoId,
		@RequestBody TodoInputTimeRequest request, @RequestAttribute("userId") Long userId) {
		Long updatedTodoId = todoService.setInputTime(todoId, request, userId);

		return ResponseEntity.created(URI.create("/api/v1/todos/" + todoId)).build();
	}

	// TODO : 아래 상태 변경 로직 수정
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
	public ResponseEntity<List<TodoResponse>> getTodosByStatus(@RequestParam TodoStatus status,
		@RequestAttribute("userId") Long userId) {
		List<TodoResponse> todoResponses = todoService.getTodosByStatus(status, userId);

		return ResponseEntity.ok().body(todoResponses);
	}

	@GetMapping("/dates")
	public ResponseEntity<List<TodoResponse>> getTodosByDate(@RequestParam LocalDate date,
		@RequestAttribute("userId") Long userId) {
		List<TodoResponse> todoResponses = todoService.getTodosByDate(date, userId);

		return ResponseEntity.ok().body(todoResponses);
	}

	@GetMapping("/categories/{category}")
	public ResponseEntity<List<TodoResponse>> getTodosByCategory(@PathVariable String category,
		@RequestAttribute("userId") Long userId) {
		List<TodoResponse> todoResponses = todoService.getTodosByCategory(category, userId);

		return ResponseEntity.ok().body(todoResponses);
	}

	@GetMapping()
	public ResponseEntity<List<TodoResponse>> getTodos(@RequestAttribute("userId") Long userId) {
		List<TodoResponse> todoResponses = todoService.getTodos(userId);

		return ResponseEntity.ok().body(todoResponses);
	}

	@GetMapping("/priorities")
	public ResponseEntity<List<TodoResponse>> getPriorities(
		@RequestAttribute("userId") Long userId) {
		List<TodoResponse> priorities = todoService.getPriorities(userId);

		return ResponseEntity.ok().body(priorities);
	}

	@GetMapping("/{todoId}/available-time")
	public ResponseEntity<AvailableTimeSlots> test(@PathVariable Long todoId) {
		AvailableTimeSlots result = todoService.findAvailableTimeSlots(todoId);

		return ResponseEntity.ok().body(result);
	}

	@PutMapping("/{todoId}/push-update")
	public ResponseEntity<String> updateTodoByPush(@RequestBody TodoPushUpdateRequest request,
		@PathVariable Long todoId, @RequestAttribute("userId") Long userId) {

		todoService.updateTodoByPush(request, todoId, userId);

		return ResponseEntity.ok().build();
	}
}
