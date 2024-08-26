package org.onesentence.onesentence.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.onesentence.onesentence.domain.chat.dto.ChatMessage;
import org.onesentence.onesentence.domain.chat.dto.ChatRoomResponse;
import org.onesentence.onesentence.domain.chat.dto.ChatTypeMessage;
import org.onesentence.onesentence.domain.chat.entity.Chat;
import org.onesentence.onesentence.domain.chat.entity.ChatRoom;
import org.onesentence.onesentence.domain.chat.entity.ChatType;
import org.onesentence.onesentence.domain.chat.repository.ChatJpaRepository;
import org.onesentence.onesentence.domain.chat.repository.ChatRoomJpaRepository;
import org.onesentence.onesentence.domain.gpt.dto.GPTAnalyzeResponse;
import org.onesentence.onesentence.domain.gpt.service.GptService;
import org.onesentence.onesentence.domain.todo.dto.AvailableTimeSlots;
import org.onesentence.onesentence.domain.todo.service.TodoService;
import org.onesentence.onesentence.global.exception.ExceptionStatus;
import org.onesentence.onesentence.global.exception.NotFoundException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

	private final ChatRoomJpaRepository chatRoomJpaRepository;
	private final ChatJpaRepository chatJpaRepository;
	private final TodoService todoService;
	private final GptService gptService;
	private final SimpMessagingTemplate simpMessagingTemplate;

	@Override
	public ChatRoomResponse createChatRoom() {
		ChatRoom chatRoom = new ChatRoom();

		ChatRoom savedChatRoom = chatRoomJpaRepository.save(chatRoom);

		return ChatRoomResponse.from(savedChatRoom);
	}

	@Override
	public void chat(ChatMessage message) throws IOException, FirebaseMessagingException {

		if (message.getType().equals("yesorno")) {
			chatJpaRepository.save(new Chat(message, ChatType.YESORNO));

			if (message.getMessage().equals("아니요")) {
				// 빈 시간대 3개 추천
				AvailableTimeSlots availableTimeSlots = todoService.findAvailableTimeSlots(
					message.getTodoId());

				simpMessagingTemplate.convertAndSend("/sub/chatroom/hanfinal", availableTimeSlots);

			} else {
				ChatTypeMessage chatTypeMessage = ChatTypeMessage.builder()
					.label("message")
					.message("일정이 확정되었습니다!")
					.build();

				log.info("일정 확정");
				simpMessagingTemplate.convertAndSend("/sub/chatroom/hanfinal", chatTypeMessage);
			}
		} else if (message.getType().equals("date")) {
			// 빈 시간대 3개 중 하나 선택해서 일정 수정

			chatJpaRepository.save(new Chat(message, ChatType.DATE));

			// DateTimeFormatter 생성 (한국어 로케일 사용)
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h시 m분",
				Locale.KOREAN);

			// 문자열을 LocalDateTime으로 파싱
			LocalDateTime dateTime = LocalDateTime.parse(message.getMessage(), formatter);

			todoService.updateTodoDate(message.getTodoId(), dateTime);

			ChatTypeMessage chatTypeMessage = ChatTypeMessage.builder()
				.label("message")
				.message("일정이 확정되었습니다!")
				.build();

			simpMessagingTemplate.convertAndSend("/sub/chatroom/hanfinal", chatTypeMessage);
		} else if (message.getType().equals("gpt")) {

			// 빈 시간대에 원하는 시간이 없는 경우 채팅방 사용자가 직접 시간 입력

			chatJpaRepository.save(new Chat(message, ChatType.GPT));

			GPTAnalyzeResponse response = gptService.gptCallForTodoCoordination(
				message.getMessage(), message.getTodoId());

			if(response.getAnalyze().equals("no") && response.getDate() != null) {
				todoService.checkTimeSlotsAndUpdateTodo(message.getTodoId(), response.getDate());
			} else if(response.getAnalyze().equals("yes")) {
				ChatTypeMessage chatTypeMessage = ChatTypeMessage.builder()
					.label("message")
					.message("일정이 확정되었습니다!")
					.build();

				simpMessagingTemplate.convertAndSend("/sub/chatroom/hanfinal", chatTypeMessage);
			}
		}

	}

	public ChatRoom findRoomById(Long id) {
		return chatRoomJpaRepository.findById(id).orElseThrow(() -> new NotFoundException(
			ExceptionStatus.NOT_FOUND));
	}
}
