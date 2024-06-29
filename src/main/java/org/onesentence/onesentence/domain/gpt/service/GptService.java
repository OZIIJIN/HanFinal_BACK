package org.onesentence.onesentence.domain.gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoResponse;

public interface GptService {

	GPTCallTodoResponse gptCall(String prompt) throws JsonProcessingException;
}
