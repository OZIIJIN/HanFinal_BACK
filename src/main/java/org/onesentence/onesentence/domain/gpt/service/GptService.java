package org.onesentence.onesentence.domain.gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoRequest;

public interface GptService {

	GPTCallTodoRequest gptCall(String prompt) throws JsonProcessingException;

	String test(String prompt);
}
