package org.onesentence.onesentence.domain.todo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.onesentence.onesentence.domain.todo.dto.TodoRequest;

@Getter
@Entity
@Table(name = "todo")
@NoArgsConstructor
@AllArgsConstructor
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
	private TodoStatus status;

	@Column
	private String location;

	@Column
	private String together;

	public Todo(TodoRequest request) {
		this.title = request.getTitle();
		this.start = request.getStart();
		this.category = request.getCategory();
		this.status = request.getStatus();
		this.end = request.getEnd();
		this.location = request.getLocation();
		this.together = request.getTogether();
	}

	public void changeToInProgress() {
		this.status = TodoStatus.IN_PROGRESS;
	}

	public void changeToDone() {
		this.status = TodoStatus.DONE;
	}

	public void updateTodo(TodoRequest request) {
		this.title = request.getTitle();
		this.start = request.getStart();
		this.category = request.getCategory();
		this.end = request.getEnd();
		this.location = request.getLocation();
		this.together = request.getTogether();
	}

	public TodoStatus setStatus(String status) {
		if(status.equals("TODO")) {
			return TodoStatus.TODO;
		} else if (status.equals("IN_PROGRESS")) {
			return TodoStatus.IN_PROGRESS;
		} else {
			return TodoStatus.DONE;
		}
	}
}
