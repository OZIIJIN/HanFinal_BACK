package org.onesentence.onesentence.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.chat.dto.ChatMessageDto;
import org.onesentence.onesentence.domain.chat.dto.ChatRoomResponseDto;
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
	public ChatRoomResponseDto createChatRoom() {
		ChatRoom chatRoom = new ChatRoom();

		ChatRoom savedChatRoom = chatRoomJpaRepository.save(chatRoom);

		return ChatRoomResponseDto.from(savedChatRoom);
	}

	@Override
	public ChatMessageDto createChat(Long roomId, ChatMessageDto message) {

		ChatRoom room = findRoomById(roomId);

		Chat chat = new Chat(message, room.getId());

		Chat savedChat = chatJpaRepository.save(chat);

		return ChatMessageDto.builder()
			.roomId(room.getId())
			.message(savedChat.getMessage())
			.build();
	}

	public ChatRoom findRoomById(Long id) {
		return chatRoomJpaRepository.findById(id).orElseThrow(() -> new NotFoundException(
			ExceptionStatus.NOT_FOUND));
	}
}
