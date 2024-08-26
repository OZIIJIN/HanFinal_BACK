package org.onesentence.onesentence.global;

import java.util.Collections;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashSet;
import java.util.Set;

@Component
public class WebSocketEventListener {

	// 동기화된 Set을 사용하여 다중 스레드 환경에서 안전하게 관리
	private final Set<String> connectedUsers = Collections.synchronizedSet(new HashSet<>());

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = headerAccessor.getSessionId();
		connectedUsers.add(sessionId);
		System.out.println("New WebSocket connection: " + sessionId);
		System.out.println("Connected users count: " + connectedUsers.size());
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = headerAccessor.getSessionId();
		connectedUsers.remove(sessionId);
		System.out.println("WebSocket disconnected: " + sessionId);
		System.out.println("Connected users count: " + connectedUsers.size());
	}

	public int getConnectedUserCount() {
		return connectedUsers.size();
	}
}
