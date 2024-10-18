package org.onesentence.onesentence.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.onesentence.onesentence.global.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

	private final LoginInterceptor loginInterceptor;

	public void addInterceptors(InterceptorRegistry registry) {
		log.info("인터셉터 등록");
		registry.addInterceptor(loginInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/api/v1/users/sign-up")
			.excludePathPatterns("**/ws-stomp")
			.excludePathPatterns("/actuator/**")
			.excludePathPatterns("/error");
	}

}
