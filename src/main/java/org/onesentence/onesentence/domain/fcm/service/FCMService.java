package org.onesentence.onesentence.domain.fcm.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import org.checkerframework.checker.units.qual.C;
import org.onesentence.onesentence.domain.chat.dto.ChatMessage;
import org.onesentence.onesentence.domain.fcm.dto.FCMSendDto;
import org.onesentence.onesentence.domain.fcm.dto.FCMWeatherDto;
import org.onesentence.onesentence.domain.todo.entity.Todo;

public interface FCMService {

	void sendMessageTo(FCMSendDto fcmSendDto) throws IOException, FirebaseMessagingException;

	void sendWeatherPushTo(FCMWeatherDto fcmWeatherDto) throws FirebaseMessagingException;

	void test(String test);

	void sendCoordinationTo(Todo todo, String start) throws FirebaseMessagingException;

	void sendNoChangeTo(Todo todo, String start) throws FirebaseMessagingException;
}
