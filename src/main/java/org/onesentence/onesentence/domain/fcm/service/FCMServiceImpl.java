package org.onesentence.onesentence.domain.fcm.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.onesentence.onesentence.domain.fcm.dto.FCMMessageDto;
import org.onesentence.onesentence.domain.fcm.dto.FCMSendDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMServiceImpl implements FCMService {

	private final FirebaseMessaging firebaseMessaging;

	@Override
	public String sendMessageTo(FCMSendDto fcmSendDto) throws IOException, FirebaseMessagingException {

		Message message = Message.builder()
			.setToken(fcmSendDto.getToken())
			.setNotification(Notification.builder()
				.setTitle(fcmSendDto.getTitle())
				.setBody(fcmSendDto.getBody())
				.build())
			.build();

		return firebaseMessaging.send(message);

	}

	@Override
	public void test(String test) {
		log.info(test);
	}
}
