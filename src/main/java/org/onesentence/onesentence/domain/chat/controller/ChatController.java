package org.onesentence.onesentence.domain.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.chat.dto.ChatMessage;
import org.onesentence.onesentence.domain.chat.dto.ChatRoomResponse;
import org.onesentence.onesentence.domain.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	@PostMapping("/api/v1/chatroom")
	public ResponseEntity<ChatRoomResponse> createChatRoom() {
		ChatRoomResponse response = chatService.createChatRoom();

		return ResponseEntity.ok().body(response);
	}

	@MessageMapping("/chatroom/hanfinal")
	public void chat (ChatMessage message) throws JsonProcessingException {

		chatService.chat(message);
	}

}
