package org.onesentence.onesentence.domain.todo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.onesentence.onesentence.domain.todo.dto.TodoDate;
import org.onesentence.onesentence.domain.todo.dto.TodoPriority;
import org.onesentence.onesentence.domain.todo.dto.TodoStatistics;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;

public interface TodoQuery {

	List<TodoPriority> calculatePriority(Long userId);

	List<Todo> getTodosByOptimalOrder(List<Long> optimalOrder);

	List<Todo> findByStatus(TodoStatus status, Long userId);

	List<Todo> findByDate(LocalDate date, Long userId);

	List<Todo> findByCategory(String category, Long userId);

	List<Todo> findAll(Long userId);

	List<TodoDate> getTodoDatesByUserId(Long userId);

	List<Todo> checkTimeSlots(Long userId, LocalDateTime date);

	List<Todo> findOverlappingTodos(Long userId, LocalDateTime startTime, LocalDateTime endTime);

	int getStatistics(Long userId);
}
