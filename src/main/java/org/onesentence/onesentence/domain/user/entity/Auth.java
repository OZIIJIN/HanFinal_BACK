package org.onesentence.onesentence.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Entity
public class Auth {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String refreshToken;

	@Column
	private Long userId;

	@Builder
	public Auth (String refreshToken, Long userId) {
		this.refreshToken = refreshToken;
		this.userId = userId;
	}

	public void refreshUpdate(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
