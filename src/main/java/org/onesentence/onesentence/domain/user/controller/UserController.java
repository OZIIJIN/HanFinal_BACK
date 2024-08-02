package org.onesentence.onesentence.domain.user.controller;

import java.net.URI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.user.dto.TokenResponseDto;
import org.onesentence.onesentence.domain.user.dto.UserLoginRequestDto;
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
	public ResponseEntity<TokenResponseDto> signUp(@RequestBody UserSignUpRequestDto request) {
		TokenResponseDto responseDto = userService.signUp(request);

		return ResponseEntity.ok(responseDto);
	}

	@PostMapping("/log-in")
	public ResponseEntity<TokenResponseDto> login(@RequestBody UserLoginRequestDto loginRequestDto) {
		TokenResponseDto responseDto = userService.login(loginRequestDto);
		return ResponseEntity.ok(responseDto);
	}

}
