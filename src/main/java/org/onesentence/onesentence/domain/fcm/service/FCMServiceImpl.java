package org.onesentence.onesentence.domain.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.onesentence.onesentence.domain.fcm.dto.FCMSendDto;
import org.springframework.stereotype.Service;

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
			.putData("todoId", fcmSendDto.getTodoId().toString())
			.build();

		return firebaseMessaging.send(message);

	}

	@Override
	public void test(String test) {
		log.info(test);
	}
}
