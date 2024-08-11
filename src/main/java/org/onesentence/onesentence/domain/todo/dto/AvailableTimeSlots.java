package org.onesentence.onesentence.domain.todo.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTimeSlots {

	private String label;

	private List<LocalDateTime> availableTimeSlots;

	public AvailableTimeSlots (List<LocalDateTime> availableTimeSlots) {
		this.availableTimeSlots = availableTimeSlots;
		this.label = "select";
	}

}
