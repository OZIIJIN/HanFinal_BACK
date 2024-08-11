package org.onesentence.onesentence.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.todo.dto.TodoDate;
import org.onesentence.onesentence.domain.todo.dto.TodoPriority;
import org.onesentence.onesentence.domain.todo.entity.QTodo;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodoQueryImpl implements TodoQuery {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<TodoPriority> calculatePriority(Long userId) {
		QTodo todo = QTodo.todo;

		NumberTemplate<Integer> durationInMinutes = Expressions.numberTemplate(Integer.class,
			"TIMESTAMPDIFF(DAY, {0}, {1})", todo.start, todo.end);

		NumberTemplate<Double> urgency = Expressions.numberTemplate(Double.class, "1.0 / ({0} + 1.0)",
			durationInMinutes);

		NumberTemplate<Double> importance = Expressions.numberTemplate(Double.class,
			"ROUND(LOG({0} + 1), 1)", todo.inputTime);

		NumberTemplate<Integer> progressTime = Expressions.numberTemplate(Integer.class,
			"GREATEST(TIMESTAMPDIFF(DAY, {0}, CURRENT_TIMESTAMP), 0)", todo.start);

		NumberTemplate<Double> priorityScore = Expressions.numberTemplate(Double.class,
			"ROUND(2 * {0} + 1 * {1} + 0.5 * {2}, 1)", urgency, importance, progressTime);

		return jpaQueryFactory
			.select(Projections.constructor(TodoPriority.class,
				todo.id.as("todoId"),
				priorityScore))
			.from(todo)
			.where(todo.status.eq(TodoStatus.TODO)
				.or(todo.status.eq(TodoStatus.IN_PROGRESS))
				.and(todo.together.isNull())
				.and(todo.userId.eq(userId)))
			.fetch();
	}

	@Override
	public List<Todo> getTodosByOptimalOrder(List<Long> optimalOrder) {
		QTodo todo = QTodo.todo;

		return jpaQueryFactory
			.selectFrom(todo)
			.where(todo.id.in(optimalOrder))
			.fetch();
	}

	@Override
	public List<Todo> findByStatus(TodoStatus status, Long userId) {
		QTodo todo = QTodo.todo;

		return jpaQueryFactory
			.selectFrom(todo)
			.where(todo.status.eq(status)
				.and(todo.userId.eq(userId)))
			.fetch();
	}

	@Override
	public List<Todo> findByDate(LocalDate date, Long userId) {
		QTodo todo = QTodo.todo;

		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

		return jpaQueryFactory
			.selectFrom(todo)
			.where(todo.start.loe(endOfDay)
				.and(todo.end.goe(startOfDay))
				.and(todo.userId.eq(userId)))
			.fetch();
	}

	@Override
	public List<Todo> findByCategory(String category, Long userId) {
		QTodo todo = QTodo.todo;

		return jpaQueryFactory
			.selectFrom(todo)
			.where(todo.category.eq(category)
				.and(todo.userId.eq(userId)))
			.fetch();
	}

	@Override
	public List<Todo> findAll(Long userId) {
		QTodo todo = QTodo.todo;

		return jpaQueryFactory
			.selectFrom(todo)
			.where(todo.userId.eq(userId))
			.fetch();
	}

	@Override
	public List<TodoDate> getTodoDatesByUserId(Long userId) {
		QTodo todo = QTodo.todo;

		return jpaQueryFactory
			.select(Projections.constructor(TodoDate.class,
				todo.id.as("todoId"),
				todo.title,
				todo.start.as("todoStart"),
				todo.end.as("todoEnd"),
				todo.inputTime))
			.from(todo)
			.where(todo.userId.eq(userId)
				.and(todo.start.after(LocalDateTime.now())))
			.orderBy(todo.start.desc())
			.fetch();
	}

}
