package org.onesentence.onesentence.domain.todo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.onesentence.onesentence.domain.todo.dto.TodoRequest;

@Getter
@Entity
@Table(name = "todo")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String title;

	@Column
	private LocalDateTime start;

	@Column
	private LocalDateTime end;

	@Column
	private String category;

	@Column
	@Enumerated(EnumType.STRING)
	private TodoStatus status;

	@Column
	private String location;

	@Column
	private String together;

	@Column
	private Integer inputTime;

	@Column
	private Long userId;

	public Todo(TodoRequest request, Long userId) {
		this.title = request.getTitle();
		this.start = LocalDateTime.of(request.getStartYear(), request.getStartMonth(),
			request.getStartDay(), request.getStartHour(), request.getStartMinute());
		this.category = request.getCategory();
		this.status = request.getStatus();
		this.end = LocalDateTime.of(request.getEndYear(), request.getEndMonth(),
			request.getEndDay(), request.getEndHour(), request.getEndMinute());
		this.location = request.getLocation();
		this.together = request.getTogether();
		this.inputTime = request.getInputTime();
		this.userId = userId;
	}

	public void changeToInProgress() {
		this.status = TodoStatus.IN_PROGRESS;
	}

	public void changeToDone() {
		this.status = TodoStatus.DONE;
	}

	public void updateTodo(TodoRequest request) {
		this.title = request.getTitle();
		this.start = LocalDateTime.of(request.getStartYear(), request.getStartMonth(),
			request.getStartDay(), request.getStartHour(), request.getStartMinute());
		this.category = request.getCategory();
		this.status = request.getStatus();
		this.end = LocalDateTime.of(request.getEndYear(), request.getEndMonth(),
			request.getEndDay(), request.getEndHour(), request.getEndMinute());
		this.location = request.getLocation();
		this.together = request.getTogether();
		this.inputTime = request.getInputTime();
	}

	public TodoStatus setStatus(String status) {
		if (status.equals("TODO")) {
			return TodoStatus.TODO;
		} else if (status.equals("IN_PROGRESS")) {
			return TodoStatus.IN_PROGRESS;
		} else {
			return TodoStatus.DONE;
		}
	}

	public void setInputTime(int inputTime) {
		this.inputTime = inputTime;
	}

	public void updateTodoDate(LocalDateTime start, LocalDateTime end) {
		this.start = start;
		this.end = end;
	}
}
