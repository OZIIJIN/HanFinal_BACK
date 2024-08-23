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

	private String start1;

	private String start2;

	private String start3;

	public AvailableTimeSlots (List<String> availableTimeSlots) {
		this.label = "date";
		this.start1 = availableTimeSlots.size() > 0 ? availableTimeSlots.get(0) : null;
		this.start2 = availableTimeSlots.size() > 1 ? availableTimeSlots.get(1) : null;
		this.start3 = availableTimeSlots.size() > 2 ? availableTimeSlots.get(2) : null;
	}

}
