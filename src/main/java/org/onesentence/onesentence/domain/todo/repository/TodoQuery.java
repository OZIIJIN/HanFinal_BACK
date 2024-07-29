package org.onesentence.onesentence.domain.todo.repository;

import com.querydsl.core.Tuple;
import java.util.List;
import org.onesentence.onesentence.domain.todo.dto.TodoPriority;
import org.onesentence.onesentence.domain.todo.dto.TodoResponse;
import org.onesentence.onesentence.domain.todo.entity.Todo;

public interface TodoQuery {

	List<TodoPriority> calculatePriority();

	List<Todo> getTodosByOptimalOrder(List<Long> optimalOrder);
}
