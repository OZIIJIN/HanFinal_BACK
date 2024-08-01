package org.onesentence.onesentence.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.onesentence.onesentence.domain.user.entity.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

	private Long userId;

	private String nickName;

	private String fcmToken;

	public static UserResponseDto from(User user){
		return new UserResponseDto(
			user.getId(),
			user.getNickName(),
			user.getFcmToken()
		);
	}

}
