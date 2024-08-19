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
		this.start1 = availableTimeSlots.get(0);
		this.start2 = availableTimeSlots.get(1);
		this.start3 = availableTimeSlots.get(2);
	}

}
