package org.onesentence.onesentence.domain.chat.service;

import org.onesentence.onesentence.domain.chat.dto.ChatMessageDto;
import org.onesentence.onesentence.domain.chat.dto.ChatRoomResponseDto;

public interface ChatService {


	ChatRoomResponseDto createChatRoom();

	ChatMessageDto createChat(ChatMessageDto message);
}
