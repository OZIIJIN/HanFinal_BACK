package org.onesentence.onesentence.global;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashSet;
import java.util.Set;

@Component
public class WebSocketEventListener {

	private final Set<String> connectedUsers = new HashSet<>();

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = headerAccessor.getSessionId();
		connectedUsers.add(sessionId);
		System.out.println("New WebSocket connection: " + sessionId);
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = headerAccessor.getSessionId();
		connectedUsers.remove(sessionId);
		System.out.println("WebSocket disconnected: " + sessionId);
	}

	public int getConnectedUserCount() {
		return connectedUsers.size();
	}
}
