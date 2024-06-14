package org.onesentence.onesentence.domain.todo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
	private LocalDateTime date;

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

	public Todo(String title, LocalDateTime date, Path path, Long categoryId, Long colorId) {
		this.title = title;
		this.date = date;
		this.path = path;
		this.categoryId = categoryId;
		this.colorId = colorId;
		this.status = TodoStatus.TODO;
	}

	public void setDue(LocalDateTime due) {
		this.date = due;
	}

	public void changeToInProgress() {
		this.status = TodoStatus.IN_PROGRESS;
	}

	public void changeToDone() {
		this.status = TodoStatus.DONE;
	}

	public void updateTodo(String title, LocalDateTime date, Long categoryId, Long colorId) {
		this.title = title;
		this.date = date;
		this.categoryId = categoryId;
		this.colorId = colorId;
	}
}
