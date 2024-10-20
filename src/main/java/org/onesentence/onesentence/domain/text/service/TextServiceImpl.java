package org.onesentence.onesentence.domain.text.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.fcm.dto.FCMSendDto;
import org.onesentence.onesentence.domain.fcm.service.FCMService;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoRequest;
import org.onesentence.onesentence.domain.gpt.service.GptService;
import org.onesentence.onesentence.domain.text.dto.TextRequest;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.service.TodoService;
import org.onesentence.onesentence.domain.todo.service.TodoServiceImpl;
import org.onesentence.onesentence.domain.user.entity.User;
import org.onesentence.onesentence.domain.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TextServiceImpl implements TextService{

	private final TodoService todoService;
	private final UserService userService;
	private final FCMService fcmService;
	private final GptService gptService;

	private static final ExecutorService GPT_EXECUTOR = Executors.newFixedThreadPool(5);
	private static final ExecutorService FCM_EXECUTOR = Executors.newFixedThreadPool(5);
	private static final ExecutorService SAVE_EXECUTOR = Executors.newFixedThreadPool(8);



	@Override
	public Long createTodoByOneSentence(TextRequest request, Long userId) throws IOException {

		userService.checkUserExistence(userId);

		fetchGPTResultsAsync(request.getText())
			.thenAcceptAsync(gptCallTodoRequest -> {

				Todo savedTodo = todoService.saveTodo(gptCallTodoRequest, userId);

				sendFcmMessageAsync(savedTodo, userId);
			}, GPT_EXECUTOR);

		// 비동기 로직이기 때문에, 임시 ID나 응답을 바로 반환
		return System.currentTimeMillis();
	}
	private void validateAndSetDefaults(GPTCallTodoRequest gptCallTodoRequest) {
		if (gptCallTodoRequest.getInputTime() == null || gptCallTodoRequest.getInputTime() == 0) {
			gptCallTodoRequest.setInputTime(1);
		}
		if (gptCallTodoRequest.getStart() == null) {
			gptCallTodoRequest.setStart(LocalDateTime.now());
		}
		if (gptCallTodoRequest.getEnd() == null) {
			gptCallTodoRequest.setEnd(
				gptCallTodoRequest.getStart().plusMinutes(60L * gptCallTodoRequest.getInputTime()));
		}
	}

	private CompletableFuture<GPTCallTodoRequest> fetchGPTResultsAsync(String text) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				GPTCallTodoRequest gptCallTodoRequest = gptService.gptCall(text);

				validateAndSetDefaults(gptCallTodoRequest);
				return gptCallTodoRequest;
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}, SAVE_EXECUTOR);
	}

	private void sendFcmMessageAsync(Todo savedTodo, Long userId) {
		CompletableFuture.runAsync(() -> {
			try {
				User user = userService.findByUserId(userId);
				FCMSendDto fcmSendDto = FCMSendDto.builder()
					.token(user.getFcmToken())
					.title("일정 등록 완료!")
					.body("[" + savedTodo.getTitle() + "] 일정이 등록되었습니다.")
					.todoId(savedTodo.getId())
					.build();
				fcmService.sendMessageTo(fcmSendDto);
			} catch (IOException | FirebaseMessagingException e) {
				// 예외 처리 로직 추가 (로그 저장 등)
				throw new RuntimeException(e);
			}
		}, FCM_EXECUTOR);
	}
}
