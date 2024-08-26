package org.onesentence.onesentence.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.io.IOException;
import org.onesentence.onesentence.domain.chat.dto.ChatMessage;
import org.onesentence.onesentence.domain.chat.dto.ChatRoomResponse;

public interface ChatService {


	ChatRoomResponse createChatRoom();

	void chat(ChatMessage message) throws IOException, FirebaseMessagingException;
}
