package org.onesentence.onesentence.domain.weather.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.fcm.dto.FCMWeatherDto;
import org.onesentence.onesentence.domain.weather.dto.WeatherDto;
import org.onesentence.onesentence.domain.weather.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weather")
public class WeatherController {

	private final WeatherService weatherService;

	@GetMapping()
	public String getCurrentWeather() throws Exception {

		return weatherService.procWeather(LocalDateTime.now());
	}

	@GetMapping("/test/{todoId}")
	public FCMWeatherDto test(@PathVariable Long todoId) throws FirebaseMessagingException {
		return weatherService.test(todoId);
	}
}
