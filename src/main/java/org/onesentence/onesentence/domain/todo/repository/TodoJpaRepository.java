package org.onesentence.onesentence.domain.todo.repository;

import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoJpaRepository extends JpaRepository<Todo, Long> {

}
