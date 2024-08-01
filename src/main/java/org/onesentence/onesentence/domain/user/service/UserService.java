package org.onesentence.onesentence.domain.user.service;

import org.onesentence.onesentence.domain.user.dto.UserRequestDto;
import org.onesentence.onesentence.domain.user.dto.UserResponseDto;

public interface UserService {

	Long signUp(UserRequestDto request);
}
