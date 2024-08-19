package org.onesentence.onesentence.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.onesentence.onesentence.domain.chat.entity.ChatRoom;

@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponse {

	private Long chatRoomId;

	public static ChatRoomResponse from (ChatRoom chatRoom) {
		return new ChatRoomResponse(
			chatRoom.getId()
		);
	}

}
