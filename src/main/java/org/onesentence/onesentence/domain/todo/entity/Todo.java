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
	private LocalDateTime todoDate;

	@Column
	private Path path;

	@Column
	private LocalDateTime due;

	@Column
	private Long categoryId;

	@Column
	private Long colorId;

	@Column
	private TodoStatus status;

	public Todo(TodoRequest request) {
		this.title = request.getTitle();
		this.todoDate = request.getTodoDate();
		this.path = request.getPath();
		this.categoryId = request.getCategoryId();
		this.colorId = request.getColorId();
		this.status = TodoStatus.TODO;
		this.due = request.getDue();
	}

	public void changeToInProgress() {
		this.status = TodoStatus.IN_PROGRESS;
	}

	public void changeToDone() {
		this.status = TodoStatus.DONE;
	}

	public void updateTodo(TodoRequest request) {
		this.title = request.getTitle();
		this.todoDate = request.getTodoDate();
		this.categoryId = request.getCategoryId();
		this.colorId = request.getColorId();
		this.due = request.getDue();
	}
}
