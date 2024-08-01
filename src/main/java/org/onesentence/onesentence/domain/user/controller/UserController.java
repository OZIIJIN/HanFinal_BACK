package org.onesentence.onesentence.domain.user.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.todo.dto.TodoRequest;
import org.onesentence.onesentence.domain.user.dto.UserRequestDto;
import org.onesentence.onesentence.domain.user.dto.UserResponseDto;
import org.onesentence.onesentence.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	@PostMapping("/signUp")
	public ResponseEntity<String> signUp(@RequestBody UserRequestDto request) {
		Long userId = userService.signUp(request);

		return ResponseEntity.created(URI.create("/api/v1/users/" + userId)).build();
	}

}
