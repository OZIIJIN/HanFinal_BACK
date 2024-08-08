package org.onesentence.onesentence.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.chat.entity.ChatRoom;
import org.onesentence.onesentence.domain.chat.repository.ChatRoomJpaRepository;

@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponseDto {

	private Long chatRoomId;

	public static ChatRoomResponseDto from (ChatRoom chatRoom) {
		return new ChatRoomResponseDto(
			chatRoom.getId()
		);
	}

}
