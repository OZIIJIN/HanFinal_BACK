package org.onesentence.onesentence.domain.weather.dto;

import lombok.Getter;

@Getter
public class WeatherDto {
	private String temperature;
	private String skyCondition;

	public WeatherDto(String temperature, String skyCondition) {
		this.temperature = temperature;
		this.skyCondition = skyCondition;
	}

}
