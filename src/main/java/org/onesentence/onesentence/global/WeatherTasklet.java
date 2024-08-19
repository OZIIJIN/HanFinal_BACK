package org.onesentence.onesentence.global;

import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.weather.service.WeatherService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeatherTasklet implements Tasklet {

	private final WeatherService weatherService;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		weatherService.processWeatherNotifications();
		return RepeatStatus.FINISHED;
	}

}
