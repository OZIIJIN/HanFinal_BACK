package org.onesentence.onesentence.domain.fcm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.onesentence.onesentence.domain.fcm.dto.FCMMessageDto;
import org.onesentence.onesentence.domain.fcm.dto.FCMSendDto;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMServiceImpl implements FCMService{

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public int sendMessageTo(FCMSendDto fcmSendDto) throws IOException {

		String message = makeMessage(fcmSendDto);

		restTemplate.getMessageConverters()
			.add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + getAccessToken());

		HttpEntity<String> entity = new HttpEntity<>(message, headers);

		String API_URL = "<https://fcm.googleapis.com/v1/projects/onesentence-446bb/messages:send>";
		ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

		return response.getStatusCode() == HttpStatus.OK ? 1 : 0;
	}

	private String getAccessToken() throws IOException {
		String firebaseConfigPath = "firebase/onesentence-dev-firebase-key.json";

		GoogleCredentials googleCredentials = GoogleCredentials
			.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
			.createScoped(List.of("<https://www.googleapis.com/auth/cloud-platform>"));

		googleCredentials.refreshIfExpired();
		return googleCredentials.getAccessToken().getTokenValue();
	}

	private String makeMessage(FCMSendDto fcmSendDto) throws JsonProcessingException {

		FCMMessageDto fcmMessageDto = FCMMessageDto.builder()
			.message(FCMMessageDto.Message.builder()
				.token(fcmSendDto.getToken())
				.notification(FCMMessageDto.Notification.builder()
					.title(fcmSendDto.getTitle())
					.body(fcmSendDto.getBody())
					.image(null)
					.build()
				).build()).validateOnly(false).build();

		return objectMapper.writeValueAsString(fcmMessageDto);
	}

	@Override
	public void test(String test) {
		log.info(test);
	}
}
