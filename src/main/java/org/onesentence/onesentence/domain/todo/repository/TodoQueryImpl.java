package org.onesentence.onesentence.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.todo.dto.TodoPriority;
import org.onesentence.onesentence.domain.todo.entity.QTodo;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodoQueryImpl implements TodoQuery {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<TodoPriority> calculatePriority() {
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
				urgency,
				importance,
				progressTime,
				priorityScore))
			.from(todo)
			.where(todo.status.eq(TodoStatus.TODO).or(todo.status.eq(TodoStatus.IN_PROGRESS)))
			.fetch();
	}
}
