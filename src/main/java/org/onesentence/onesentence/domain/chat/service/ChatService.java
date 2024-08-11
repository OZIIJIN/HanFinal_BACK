package org.onesentence.onesentence.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.onesentence.onesentence.domain.chat.dto.ChatMessage;
import org.onesentence.onesentence.domain.chat.dto.ChatRoomResponse;

public interface ChatService {


	ChatRoomResponse createChatRoom();

	void chat(ChatMessage message) throws JsonProcessingException;
}
