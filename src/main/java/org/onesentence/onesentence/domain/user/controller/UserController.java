package org.onesentence.onesentence.domain.user.controller;

import java.net.URI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.user.dto.TokenResponseDto;
import org.onesentence.onesentence.domain.user.dto.UserLoginRequestDto;
import org.onesentence.onesentence.domain.user.dto.UserResponseDto;
import org.onesentence.onesentence.domain.user.dto.UserSignUpRequestDto;
import org.onesentence.onesentence.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	@PostMapping("/sign-up")
	public ResponseEntity<UserResponseDto> signUp(@RequestBody UserSignUpRequestDto request) {
		UserResponseDto responseDto = userService.signUp(request);

		return ResponseEntity.ok(responseDto);
	}

}
