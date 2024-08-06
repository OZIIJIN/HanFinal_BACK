package org.onesentence.onesentence.domain.fcm.service;

import java.io.IOException;
import org.onesentence.onesentence.domain.fcm.dto.FCMSendDto;
import org.springframework.stereotype.Service;

public interface FCMService {

	int sendMessageTo(FCMSendDto fcmSendDto) throws IOException;

	void test(String test);
}
