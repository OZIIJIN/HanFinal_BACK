package org.onesentence.onesentence.domain.user.service;

import org.onesentence.onesentence.domain.user.dto.TokenResponseDto;
import org.onesentence.onesentence.domain.user.dto.UserLoginRequestDto;
import org.onesentence.onesentence.domain.user.dto.UserResponseDto;
import org.onesentence.onesentence.domain.user.dto.UserSignUpRequestDto;
import org.onesentence.onesentence.domain.user.entity.User;

public interface UserService {

	UserResponseDto signUp(UserSignUpRequestDto request);

	User findByUserId(Long userId);

	void checkUserExistence(Long userId);

}
