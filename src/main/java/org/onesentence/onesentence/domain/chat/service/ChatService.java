package org.onesentence.onesentence.domain.chat.service;

import org.onesentence.onesentence.domain.chat.dto.ChatMessage;
import org.onesentence.onesentence.domain.chat.dto.ChatRoomResponse;

public interface ChatService {


	ChatRoomResponse createChatRoom();

	ChatMessage createChat(ChatMessage message);
}
