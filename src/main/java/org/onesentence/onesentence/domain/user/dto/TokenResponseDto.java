package org.onesentence.onesentence.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.onesentence.onesentence.domain.user.entity.Auth;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDto {

	private String accessToken;

	private String refreshToken;

}
