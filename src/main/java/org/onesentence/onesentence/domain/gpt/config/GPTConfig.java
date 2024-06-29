package org.onesentence.onesentence.domain.gpt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GPTConfig {

	@Value("${openai.api.key}")
	private String key;

	@Bean
	public RestTemplate template() {
		RestTemplate template = new RestTemplate();

		template.getInterceptors().add((request, body, execution) -> {
			request.getHeaders().add(
				"Authorization"
				,"Bearer "+key);
			return execution.execute(request,body);
		});
		return template;
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		return mapper;
	}
}
