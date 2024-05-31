package org.onesentence.onesentence.domain.gpt.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GPTResponse {

	private List<Choice> choices;

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Choice {

		private int index;

		private Message message;

	}

}
