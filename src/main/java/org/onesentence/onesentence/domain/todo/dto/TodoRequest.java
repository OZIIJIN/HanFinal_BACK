package org.onesentence.onesentence.domain.todo.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoRequest;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequest {

	private String title;

	private int startYear;

	private int startMonth;

	private int startDay;

	private int startHour;

	private int startMinute;

	private int endYear;

	private int endMonth;

	private int endDay;

	private int endHour;

	private int endMinute;

	private String category;

	private TodoStatus status;

	private String location;

	private String together;

	private int inputTime;
}
