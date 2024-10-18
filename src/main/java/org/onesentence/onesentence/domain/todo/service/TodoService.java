package org.onesentence.onesentence.domain.todo.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.onesentence.onesentence.domain.todo.dto.AvailableTimeSlots;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoRequest;
import org.onesentence.onesentence.domain.todo.dto.*;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;
import org.quartz.SchedulerException;

public interface TodoService {

	Long createTodo(TodoRequest request, Long userId) throws SchedulerException;

	Long updateTodo(TodoRequest request, Long todoId, Long userId);

	void deleteTodo(Long todoId, Long userId);

	Long updateStatus(TodoStatusRequest request, Long todoId);

	TodoResponse getTodo(Long todoId);

	List<TodoResponse> getTodosByStatus(TodoStatus status, Long userId);

	List<TodoResponse> getTodosByDate(LocalDate date, Long userId);

	List<TodoResponse> getTodosByCategory(String category, Long userId);

	List<TodoResponse> getTodos(Long userId);

	List<TodoResponse> getPriorities(Long userId);

	Long createTodoByOneSentence(TextRequest request, Long userId)
		throws IOException, FirebaseMessagingException;

	Long setInputTime(Long todoId, TodoInputTimeRequest request, Long userId);

	void coordinateTodo(Long todoId, Long userId) throws SchedulerException;

	List<TodoDate> getTodoDatesByUserId(Long todoId);

	Todo findById(Long todoId);

	AvailableTimeSlots findAvailableTimeSlots(Long todoId);

	void checkTimeSlotsAndUpdateTodo(Long todoId, LocalDateTime date)
		throws IOException, FirebaseMessagingException;

	void updateTodoDate(Long todoId, LocalDateTime start);

	String findRecommendedTimeSlot(Todo targetTodo);

	String dateConvertToString(LocalDateTime localDateTime);

	void updateTodoByPush(TodoPushUpdateRequest request, Long todoId, Long userId);

	TodoStatistics getStatistics(Long userId);
}
