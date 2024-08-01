package org.onesentence.onesentence.domain.fcm.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.onesentence.onesentence.domain.fcm.dto.FCMSendDto;
import org.onesentence.onesentence.domain.fcm.service.FCMService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fcm")
public class FCMController {

	private final FCMService fcmService;

	@PostMapping("/send")
	public ResponseEntity<Integer> pushMessage(
		@RequestBody @Validated FCMSendDto fcmSendDto) throws IOException {

		log.debug("[+] 푸시 메시지를 전송합니다. ");
		int result = fcmService.sendMessageTo(fcmSendDto);

		return ResponseEntity.ok().body(result);
	}
}
