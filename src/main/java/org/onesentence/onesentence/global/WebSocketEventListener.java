package org.onesentence.onesentence.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.chat.dto.CoordinationMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Getter
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

	private final RabbitTemplate rabbitTemplate;
	private final SimpMessagingTemplate simpMessagingTemplate;

	private int connectedUserCount = 0;

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectEvent event) {
		connectedUserCount++;
		System.out.println("사용자가 입장했습니다. 현재 연결된 사용자 수: " + connectedUserCount);

		Long todoId = extractTodoIdFromEvent(event);

		String queueName = "todo-" + todoId + "-queue";
		sendMessagesFromQueue(queueName);
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		if (connectedUserCount > 0) {
			connectedUserCount--;
		}
		System.out.println("사용자가 퇴장했습니다. 현재 연결된 사용자 수: " + connectedUserCount);
	}

	public boolean isUserConnected() {
		return connectedUserCount > 0;
	}

	private Long extractTodoIdFromEvent(SessionConnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String destination = headerAccessor.getDestination();

		if (destination != null && destination.startsWith("/todo/")) {
			String todoIdStr = destination.substring("/todo/".length());
			return Long.parseLong(todoIdStr);
		}

		return null;
	}

	private void sendMessagesFromQueue(String queueName) {
		while (connectedUserCount > 0) {
			CoordinationMessage message = (CoordinationMessage) rabbitTemplate.receiveAndConvert(
				queueName);
			if (message != null) {
				String destination = "/sub/chatroom/" + message.getTodoId();
				simpMessagingTemplate.convertAndSend(destination, message);
			} else {
				break;
			}
		}
	}

}
