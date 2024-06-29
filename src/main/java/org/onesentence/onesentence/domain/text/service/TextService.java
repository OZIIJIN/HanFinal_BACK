package org.onesentence.onesentence.domain.text.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.onesentence.onesentence.domain.text.dto.TextRequest;
import org.springframework.http.ResponseEntity;

public interface TextService {

	Long createText(TextRequest request) throws JsonProcessingException;
}
