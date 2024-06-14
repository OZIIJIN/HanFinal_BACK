package org.onesentence.onesentence.domain.todo.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class TodoRequest {

	@NotNull
	private String title;

	private LocalDateTime date;

	private LocalDateTime due;

	private Long categoryId;

	private Long colorId;
}
