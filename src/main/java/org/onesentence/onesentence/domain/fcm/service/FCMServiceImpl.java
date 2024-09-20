package org.onesentence.onesentence.domain.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.onesentence.onesentence.domain.chat.dto.ChatMessage;
import org.onesentence.onesentence.domain.fcm.dto.FCMSendDto;
import org.onesentence.onesentence.domain.fcm.dto.FCMWeatherDto;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.service.TodoService;
import org.onesentence.onesentence.domain.user.entity.User;
import org.onesentence.onesentence.domain.user.service.UserService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMServiceImpl implements FCMService {

	private final FirebaseMessaging firebaseMessaging;
	private final UserService userService;

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
	public void sendWeatherPushTo(FCMWeatherDto fcmWeatherDto) throws FirebaseMessagingException {
		Message message = Message.builder()
			.setToken(fcmWeatherDto.getToken())
			.setNotification(Notification.builder()
				.setTitle(fcmWeatherDto.getTitle())
				.setBody(fcmWeatherDto.getBody())
				.build())
			.putData("todoId", fcmWeatherDto.getTodoId().toString())
			.putData("date", fcmWeatherDto.getDate())
			.putData("type", fcmWeatherDto.getType())
			.putData("todoTitle", fcmWeatherDto.getTodoTitle())
			.build();

		firebaseMessaging.send(message);
	}

	@Override
	public void test(String test) {
		log.info(test);
	}

	@Override
	public void sendCoordinationTo(Todo todo, String start) throws FirebaseMessagingException {
		User user = userService.findByUserId(todo.getUserId());

		Message message = Message.builder()
			.setToken(user.getFcmToken())
			.setNotification(Notification.builder()
				.setTitle("한끝봇에 의해 일정이 조율되었습니다!")
				.setBody("[" + todo.getTitle() + "] " + start + " 으로 일정이 변경되었습니다.")
				.build())
			.putData("todoId", todo.getId().toString())
			.build();

		firebaseMessaging.send(message);
	}

	@Override
	public void sendNoChangeTo(Todo todo, String start) throws FirebaseMessagingException {
		User user = userService.findByUserId(todo.getUserId());

		Message message = Message.builder()
			.setToken(user.getFcmToken())
			.setNotification(Notification.builder()
				.setTitle("한끝봇에 의해 일정이 조율되었습니다!")
				.setBody("[" + todo.getTitle() + "] " +start + " 으로 기존 일정대로 확정되었습니다.")
				.build())
			.putData("todoId", todo.getId().toString())
			.build();

		firebaseMessaging.send(message);
	}
}
