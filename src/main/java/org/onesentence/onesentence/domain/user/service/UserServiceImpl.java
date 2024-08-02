package org.onesentence.onesentence.domain.user.service;

import static org.onesentence.onesentence.global.exception.ExceptionStatus.DUPLICATED_NICKNAME;

import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.user.dto.TokenResponseDto;
import org.onesentence.onesentence.domain.user.dto.UserLoginRequestDto;
import org.onesentence.onesentence.domain.user.dto.UserSignUpRequestDto;
import org.onesentence.onesentence.domain.user.entity.Auth;
import org.onesentence.onesentence.domain.user.entity.User;
import org.onesentence.onesentence.domain.user.repository.AuthJpaRepository;
import org.onesentence.onesentence.domain.user.repository.UserJpaRepository;
import org.onesentence.onesentence.global.exception.DuplicatedException;
import org.onesentence.onesentence.global.jwt.TokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserJpaRepository userJpaRepository;
	private final TokenUtils tokenUtils;
	private final AuthJpaRepository authJpaRepository;


	@Override
	@Transactional
	public TokenResponseDto signUp(UserSignUpRequestDto request) {
		checkIfNickNameAlreadyExists(request.getNickName());

		User user = new User(request);

		User savedUser = userJpaRepository.save(user);

		String accessToken = tokenUtils.generateJwtToken(savedUser);
		String refreshToken = tokenUtils.saveRefreshToken(savedUser);

		authJpaRepository.save(
			Auth.builder().userId(savedUser.getId()).refreshToken(refreshToken).build());

		return TokenResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken)
			.build();
	}

	private void checkIfNickNameAlreadyExists(String nickName) {
		if (userJpaRepository.existsByNickName(nickName)) {
			throw new DuplicatedException(DUPLICATED_NICKNAME);
		}
	}

	@Transactional
	public TokenResponseDto login(UserLoginRequestDto loginRequestDto) {
		User user =
			userJpaRepository.findByNickName(loginRequestDto.getNickName())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

		Auth auth =
			authJpaRepository.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalArgumentException("Token 이 존재하지 않습니다."));

		String accessToken = "";
		String refreshToken = auth.getRefreshToken();

		if (tokenUtils.isValidRefreshToken(refreshToken)) {
			User userByAuth =
				userJpaRepository.findById(auth.getUserId())
					.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

			accessToken = tokenUtils.generateJwtToken(userByAuth);

			return TokenResponseDto.builder()
				.accessToken(accessToken)
				.refreshToken(auth.getRefreshToken())
				.build();
		} else {
			refreshToken = tokenUtils.saveRefreshToken(user);
			auth.refreshUpdate(refreshToken);
		}

		return TokenResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken)
			.build();
	}
}
