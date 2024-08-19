package org.onesentence.onesentence.domain.user.service;

import static org.onesentence.onesentence.global.exception.ExceptionStatus.DUPLICATED_NICKNAME;
import static org.onesentence.onesentence.global.exception.ExceptionStatus.NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.user.dto.UserResponseDto;
import org.onesentence.onesentence.domain.user.dto.UserSignUpRequestDto;
import org.onesentence.onesentence.domain.user.entity.User;
import org.onesentence.onesentence.domain.user.repository.UserJpaRepository;
import org.onesentence.onesentence.global.exception.DuplicatedException;
import org.onesentence.onesentence.global.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserJpaRepository userJpaRepository;

	@Override
	@Transactional
	public UserResponseDto signUp(UserSignUpRequestDto request) {
		checkIfNickNameAlreadyExists(request.getNickName());

		User user = new User(request);

		User savedUser = userJpaRepository.save(user);

		return new UserResponseDto(
			savedUser.getId(),
			savedUser.getNickName(),
			savedUser.getFcmToken()
		);
	}

	@Override
	@Transactional(readOnly = true)
	public User findByUserId(Long userId) {
		return userJpaRepository.findById(userId).orElseThrow(() -> new NotFoundException(NOT_FOUND));
	}

	private void checkIfNickNameAlreadyExists(String nickName) {
		if (userJpaRepository.existsByNickName(nickName)) {
			throw new DuplicatedException(DUPLICATED_NICKNAME);
		}
	}
}
