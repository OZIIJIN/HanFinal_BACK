package org.onesentence.onesentence.domain.user.service;

import static org.onesentence.onesentence.global.exception.ExceptionStatus.DUPLICATED_NICKNAME;

import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.user.dto.UserRequestDto;
import org.onesentence.onesentence.domain.user.entity.User;
import org.onesentence.onesentence.domain.user.repository.UserJpaRepository;
import org.onesentence.onesentence.global.exception.DuplicatedException;
import org.onesentence.onesentence.global.exception.ExceptionStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final UserJpaRepository userJpaRepository;

	@Override
	@Transactional
	public Long signUp(UserRequestDto request) {
		checkIfNickNameAlreadyExists(request.getNickName());

		User user = new User(request);

		User savedUser = userJpaRepository.save(user);

		return savedUser.getId();
	}

	private void checkIfNickNameAlreadyExists(String nickName) {
		if (userJpaRepository.existsByNickName(nickName)) {
			throw new DuplicatedException(DUPLICATED_NICKNAME);
		}
	}
}
