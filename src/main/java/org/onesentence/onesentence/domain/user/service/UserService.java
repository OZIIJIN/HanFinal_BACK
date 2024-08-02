package org.onesentence.onesentence.domain.user.service;

import org.onesentence.onesentence.domain.user.dto.TokenResponseDto;
import org.onesentence.onesentence.domain.user.dto.UserLoginRequestDto;
import org.onesentence.onesentence.domain.user.dto.UserSignUpRequestDto;

public interface UserService {

	TokenResponseDto signUp(UserSignUpRequestDto request);

	TokenResponseDto login(UserLoginRequestDto loginRequestDto);
}
