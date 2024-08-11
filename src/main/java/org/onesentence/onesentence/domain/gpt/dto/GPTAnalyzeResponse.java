package org.onesentence.onesentence.domain.gpt.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GPTAnalyzeResponse {

	private String analyze;

	private LocalDateTime date;

}
