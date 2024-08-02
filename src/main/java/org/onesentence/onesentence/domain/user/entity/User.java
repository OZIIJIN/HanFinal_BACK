package org.onesentence.onesentence.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.onesentence.onesentence.domain.user.dto.UserSignUpRequestDto;

@Entity
@Getter
@Builder
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

	public User(UserSignUpRequestDto userSignUpRequestDto) {
		this.nickName = userSignUpRequestDto.getNickName();
		this.fcmToken = userSignUpRequestDto.getFcmToken();
	}
}
