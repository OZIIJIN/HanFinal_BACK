package org.onesentence.onesentence.domain.todo.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.onesentence.onesentence.domain.todo.entity.Path;

@Getter
@NoArgsConstructor
public class TodoRequest {

	private String title;

	private LocalDateTime date;

	private Path path;

	private LocalDateTime due;

	private Long categoryId;

	private Long colorId;
}
