package org.onesentence.onesentence.domain.fcm.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import java.io.IOException;
import org.onesentence.onesentence.domain.fcm.dto.FCMSendDto;
import org.onesentence.onesentence.domain.fcm.dto.FCMWeatherDto;

public interface FCMService {

	String sendMessageTo(FCMSendDto fcmSendDto) throws IOException, FirebaseMessagingException;

	void sendWeatherPushTo(FCMWeatherDto fcmWeatherDto) throws FirebaseMessagingException;

	void test(String test);
}
