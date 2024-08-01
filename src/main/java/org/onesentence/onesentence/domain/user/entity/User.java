package org.onesentence.onesentence.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.onesentence.onesentence.domain.user.dto.UserRequestDto;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String nickName;

	@Column
	private String fcmToken;

	public User(UserRequestDto userRequestDto) {
		this.nickName = userRequestDto.getNickName();
		this.fcmToken = userRequestDto.getFcmToken();
	}

}
