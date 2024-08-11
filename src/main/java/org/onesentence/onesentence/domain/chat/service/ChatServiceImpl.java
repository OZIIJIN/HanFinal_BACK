package org.onesentence.onesentence.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.chat.dto.ChatMessage;
import org.onesentence.onesentence.domain.chat.dto.ChatRoomResponse;
import org.onesentence.onesentence.domain.chat.entity.Chat;
import org.onesentence.onesentence.domain.chat.entity.ChatRoom;
import org.onesentence.onesentence.domain.chat.repository.ChatJpaRepository;
import org.onesentence.onesentence.domain.chat.repository.ChatRoomJpaRepository;
import org.onesentence.onesentence.global.exception.ExceptionStatus;
import org.onesentence.onesentence.global.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

	private final ChatRoomJpaRepository chatRoomJpaRepository;
	private final ChatJpaRepository chatJpaRepository;

	@Override
	public ChatRoomResponse createChatRoom() {
		ChatRoom chatRoom = new ChatRoom();

		ChatRoom savedChatRoom = chatRoomJpaRepository.save(chatRoom);

		return ChatRoomResponse.from(savedChatRoom);
	}

	@Override
	public ChatMessage createChat(ChatMessage message) {

		Chat chat = new Chat(message);

		Chat savedChat = chatJpaRepository.save(chat);

		return ChatMessage.builder()
			.message(savedChat.getMessage())
			.build();
	}

	public ChatRoom findRoomById(Long id) {
		return chatRoomJpaRepository.findById(id).orElseThrow(() -> new NotFoundException(
			ExceptionStatus.NOT_FOUND));
	}
}
