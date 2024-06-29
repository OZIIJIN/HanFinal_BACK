package org.onesentence.onesentence.domain.todo.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoResponse;
import org.onesentence.onesentence.domain.todo.entity.Path;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequest {

	private String title;

	private LocalDateTime start;

	private LocalDateTime end;

	private String category;

	private TodoStatus status;

	private String location;

	private String together;

	public static TodoRequest gptResponseToRequest(GPTCallTodoResponse gptResponse) {
		return new TodoRequest(
			gptResponse.getTitle(),
			gptResponse.getStart(),
			gptResponse.getEnd(),
			gptResponse.getCategory(),
			TodoStatus.TODO,
			gptResponse.getLocation(),
			gptResponse.getTogether()
		);
	}
}
