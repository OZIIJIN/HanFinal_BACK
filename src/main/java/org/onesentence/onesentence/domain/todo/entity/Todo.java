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
	private Path path;

	@Column
	private LocalDateTime end;

	@Column
	private Long categoryId;

	@Column
	private TodoStatus status;

	@Column
	private String location;

	@Column
	private String together;

	public Todo(TodoRequest request) {
		this.title = request.getTitle();
		this.start = request.getStart();
		this.path = request.getPath();
		this.categoryId = request.getCategoryId();
		this.status = TodoStatus.TODO;
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
		this.categoryId = request.getCategoryId();
		this.end = request.getEnd();
	}
}
