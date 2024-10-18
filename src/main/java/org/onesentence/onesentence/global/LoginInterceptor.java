package org.onesentence.onesentence.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.onesentence.onesentence.domain.user.entity.User;
import org.onesentence.onesentence.domain.user.repository.UserJpaRepository;
import org.onesentence.onesentence.global.exception.ExceptionStatus;
import org.onesentence.onesentence.global.exception.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

	private final UserJpaRepository userJpaRepository;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler)
		throws IOException {

		String requestURI = request.getRequestURI();

		if (requestURI.contains("/actuator")) {
			return true;
		}

		String nickName = request.getHeader("NICKNAME");
		log.info("User NickName: " + nickName);

		try {
			if (nickName != null) {
				User user = userJpaRepository.findByNickName(nickName)
					.orElseThrow(() -> new NotFoundException(ExceptionStatus.NOT_FOUND));
				request.setAttribute("userId", user.getId());
				return true;
			} else {
				throw new NotFoundException(ExceptionStatus.NOT_FOUND);
			}
		} catch (NotFoundException e) {
			response.setStatus(e.getStatusCode());
			response.setHeader("message", e.getMessage());
			return false;
		}
	}
}
