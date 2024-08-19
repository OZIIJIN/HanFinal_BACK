package org.onesentence.onesentence.domain.text.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.onesentence.onesentence.domain.text.dto.TextRequest;

public interface TextService {

	Long createTodoByOneSentence(TextRequest request, Long userId) throws JsonProcessingException;
  
}
