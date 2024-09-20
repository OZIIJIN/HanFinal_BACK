package org.onesentence.onesentence.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.todo.dto.TodoDate;
import org.onesentence.onesentence.domain.todo.dto.TodoPriority;
import org.onesentence.onesentence.domain.todo.dto.TodoStatistics;
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

		// 일정의 시작 시간과 종료 시간 사이의 일(day) 단위 차이를 계산합니다.
		NumberTemplate<Integer> durationInMinutes = Expressions.numberTemplate(Integer.class,
			"TIMESTAMPDIFF(DAY, {0}, {1})", todo.start, todo.end);

		// 긴급도를 계산합니다. 일정 기간이 짧을수록 긴급도가 높아집니다.
		NumberTemplate<Double> urgency = Expressions.numberTemplate(Double.class,
			"1.0 / ({0} + 1.0)",
			durationInMinutes);

		// 중요도를 계산합니다. 소요 시간을 로그 함수로 변환하여 증가율이 점차 줄어들도록 합니다.
		NumberTemplate<Double> importance = Expressions.numberTemplate(Double.class,
			"ROUND(LOG({0} + 1), 1)", todo.inputTime);

		// 일정의 시작 시간과 현재 시간 사이의 경과 시간을 계산합니다. 최소값은 0입니다.
		NumberTemplate<Integer> progressTime = Expressions.numberTemplate(Integer.class,
			"GREATEST(TIMESTAMPDIFF(DAY, {0}, CURRENT_TIMESTAMP), 0)", todo.start);

		// 긴급도, 중요도, 경과 시간을 가중치로 계산하여 우선순위 점수를 산출합니다.
		NumberTemplate<Double> priorityScore = Expressions.numberTemplate(Double.class,
			"ROUND(2 * {0} + 1 * {1} + 0.5 * {2}, 1)", urgency, importance, progressTime);

		return jpaQueryFactory
			.select(Projections.constructor(TodoPriority.class,
				todo.id.as("todoId"),
				priorityScore))  // TodoPriority 객체를 생성할 때 사용될 필드들을 선택합니다.
			.from(todo)  // 'todo' 테이블을 대상으로 합니다.
			.where(todo.status.eq(TodoStatus.TODO)  // 'TODO' 상태의 일정이거나
				.or(todo.status.eq(TodoStatus.IN_PROGRESS))  // 'IN_PROGRESS' 상태의 일정이어야 하며
				.and(todo.together.isNull())  // 함께할 사람이 지정되지 않은 일정이어야 하고
				.and(todo.userId.eq(userId)))  // 주어진 userId와 일치하는 일정이어야 합니다.
			.fetch();  // 조건에 맞는 결과를 가져옵니다.
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

	@Override
	public List<Todo> checkTimeSlots(Long userId, LocalDateTime date) {
		QTodo todo = QTodo.todo;

		LocalDate targetDate = date.toLocalDate();

		return jpaQueryFactory
			.selectFrom(todo)
			.where(todo.userId.eq(userId)
				.and(todo.start.between(
					targetDate.atStartOfDay(),
					targetDate.atTime(23, 59, 59))))
			.fetch();
	}

	@Override
	public List<Todo> findOverlappingTodos(Long userId, LocalDateTime startTime,
		LocalDateTime endTime) {
		QTodo todo = QTodo.todo;

		return jpaQueryFactory.selectFrom(todo)
			.where(
				todo.userId.eq(userId)
					.and(todo.start.lt(endTime))   // 시작 시간이 endTime보다 작아야 겹침
					.and(todo.end.gt(startTime))   // 종료 시간이 startTime보다 커야 겹침
			)
			.fetch();
	}

	@Override
	public int getStatistics(Long userId) {
		QTodo todo = QTodo.todo;

		Long totalTodosCount = jpaQueryFactory.select(todo.count())
			.from(todo)
			.where(todo.userId.eq(userId))
			.fetchOne();

		Long doneTodosCount = jpaQueryFactory.select(todo.count())
			.from(todo)
			.where(todo.userId.eq(userId)
				.and(todo.status.eq(TodoStatus.DONE)))
			.fetchOne();

		totalTodosCount = totalTodosCount != null ? totalTodosCount : 0;
		doneTodosCount = doneTodosCount != null ? doneTodosCount : 0;

		if (totalTodosCount == 0) {
			return 0; // 전체 투두 수가 0일 때는 0% 반환
		}

		return (int) ((doneTodosCount / (double) totalTodosCount) * 100);
	}

}
