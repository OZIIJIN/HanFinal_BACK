package org.onesentence.onesentence.domain.todo.repository;

import com.querydsl.core.Tuple;
import java.util.List;
import org.onesentence.onesentence.domain.todo.dto.TodoPriority;

public interface TodoQuery {

	List<TodoPriority> calculatePriority();
}
