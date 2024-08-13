package org.onesentence.onesentence.domain.weather.controller;

import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.weather.dto.WeatherDto;
import org.onesentence.onesentence.domain.weather.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weather")
public class WeatherController {

	private final WeatherService weatherService;

	@GetMapping()
	public String getCurrentWeather() {
		try {
			return weatherService.procWeather();
		} catch (Exception e) {
			return "Error occurred: " + e.getMessage();
		}
	}

}
