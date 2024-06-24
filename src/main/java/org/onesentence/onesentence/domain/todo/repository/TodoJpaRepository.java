package org.onesentence.onesentence.domain.todo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoJpaRepository extends JpaRepository<Todo, Long> {

	List<Todo> findByStatus(TodoStatus status);

	List<Todo> findByDateBetween(LocalDateTime start, LocalDateTime end);

	List<Todo> findByCategoryId(Long categoryId);

}
