package org.onesentence.onesentence.domain.todo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoJpaRepository extends JpaRepository<Todo, Long> {

}
