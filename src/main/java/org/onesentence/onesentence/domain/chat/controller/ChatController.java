package org.onesentence.onesentence.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.chat.dto.ChatMessageDto;
import org.onesentence.onesentence.domain.chat.dto.ChatRoomResponseDto;
import org.onesentence.onesentence.domain.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	@PostMapping("/api/v1/chat")
	public ResponseEntity<ChatRoomResponseDto> createChatRoom() {
		ChatRoomResponseDto response = chatService.createChatRoom();

		return ResponseEntity.ok().body(response);
	}

	@MessageMapping("/chatroom/{roomId}")
	@SendTo("/chatroom/{roomId}")
	public ChatMessageDto chat (@DestinationVariable Long roomId, ChatMessageDto message) {

		return chatService.createChat(roomId, message);
	}

}
