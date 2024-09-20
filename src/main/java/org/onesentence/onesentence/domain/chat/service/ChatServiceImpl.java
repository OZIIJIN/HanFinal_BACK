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
import org.onesentence.onesentence.domain.fcm.dto.FCMSendDto;
import org.onesentence.onesentence.domain.fcm.service.FCMService;
import org.onesentence.onesentence.domain.gpt.dto.GPTAnalyzeResponse;
import org.onesentence.onesentence.domain.gpt.service.GptService;
import org.onesentence.onesentence.domain.todo.dto.AvailableTimeSlots;
import org.onesentence.onesentence.domain.todo.entity.Todo;
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
	private final FCMService fcmService;
	private final SimpMessagingTemplate simpMessagingTemplate;

	@Override
	public ChatRoomResponse createChatRoom() {
		ChatRoom chatRoom = new ChatRoom();

		ChatRoom savedChatRoom = chatRoomJpaRepository.save(chatRoom);

		return ChatRoomResponse.from(savedChatRoom);
	}

	@Override
	public void chat(ChatMessage message) throws IOException, FirebaseMessagingException {

		// "yesorno" 타입의 메시지인 경우
		if (message.getType().equals("yesorno")) {
			// Chat 엔티티로 변환하여 데이터베이스에 저장
			chatJpaRepository.save(new Chat(message, ChatType.YESORNO));

			// 메시지가 "아니요"인 경우
			if (message.getMessage().equals("아니요")) {
				// 빈 시간대 3개를 추천받아 전송
				AvailableTimeSlots availableTimeSlots = todoService.findAvailableTimeSlots(
					message.getTodoId());

				// 채팅방에 추천된 시간대를 전송
				simpMessagingTemplate.convertAndSend("/sub/chatroom/hanfinal", availableTimeSlots);

			} else {
				// "예" 또는 다른 메시지인 경우 일정이 확정되었음을 알림
				Todo todo = todoService.findById(message.getTodoId());
				String start = todoService.dateConvertToString(todo.getStart());

				ChatTypeMessage chatTypeMessage = ChatTypeMessage.builder()
					.label("message")
					.message("일정이 확정되었습니다!")
					.build();

				log.info("일정 확정");

				// 채팅방에 일정 확정 메시지를 전송
				simpMessagingTemplate.convertAndSend("/sub/chatroom/hanfinal", chatTypeMessage);
				fcmService.sendNoChangeTo(todo, start);
			}

			// "date" 타입의 메시지인 경우
		} else if (message.getType().equals("date")) {
			// 빈 시간대 중 하나를 선택하여 일정 수정
			chatJpaRepository.save(new Chat(message, ChatType.DATE));

			// 한국어 로케일을 사용하는 DateTimeFormatter를 생성
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h시 mm분",
				Locale.KOREAN);

			// 메시지로 받은 날짜 문자열을 LocalDateTime 객체로 변환
			LocalDateTime dateTime = LocalDateTime.parse(message.getMessage(), formatter);

			// 선택한 날짜로 일정 수정
			todoService.updateTodoDate(message.getTodoId(), dateTime);

			Todo todo = todoService.findById(message.getTodoId());
			String start = todoService.dateConvertToString(todo.getStart());
			// 일정 확정 메시지를 생성하고 전송
			ChatTypeMessage chatTypeMessage = ChatTypeMessage.builder()
				.label("message")
				.message("일정이 확정되었습니다!")
				.build();

			simpMessagingTemplate.convertAndSend("/sub/chatroom/hanfinal", chatTypeMessage);
			fcmService.sendCoordinationTo(todo, start);
			// "gpt" 타입의 메시지인 경우
		} else if (message.getType().equals("gpt")) {
			// GPT로 분석 요청 후 필요한 작업을 수행
			chatJpaRepository.save(new Chat(message, ChatType.GPT));

			// GPT 서비스를 호출하여 일정 조정 관련 분석을 수행
			GPTAnalyzeResponse response = gptService.gptCallForTodoCoordination(
				message.getMessage(), message.getTodoId());

			// GPT 분석 결과가 "no"이고 추천된 날짜가 있는 경우
			if(response.getAnalyze().equals("no") && response.getDate() != null) {
				// 추천된 날짜로 일정 업데이트
				todoService.checkTimeSlotsAndUpdateTodo(message.getTodoId(), response.getDate());

				// GPT 분석 결과가 "yes"인 경우
			} else if(response.getAnalyze().equals("yes")) {
				// 일정이 확정되었음을 알림
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
