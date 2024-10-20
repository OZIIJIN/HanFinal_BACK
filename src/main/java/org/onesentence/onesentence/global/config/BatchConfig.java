package org.onesentence.onesentence.global.config;

import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.fcm.dto.FCMWeatherDto;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.global.TodoItemProcessor;
import org.onesentence.onesentence.global.TodoItemReader;
import org.onesentence.onesentence.global.TodoItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class BatchConfig {

	private final JobLauncher jobLauncher;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final TodoItemReader todoItemReader;
	private final TodoItemProcessor todoItemProcessor;
	private final TodoItemWriter todoItemWriter;

	@Bean
	public Job weatherNotificationJob() {
		return new JobBuilder("weatherNotificationJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(weatherNotificationStep())
			.build();
	}

	@Bean
	public Step weatherNotificationStep() {
		return new StepBuilder("weatherNotificationStep", jobRepository)
			.<Todo, FCMWeatherDto>chunk(10, transactionManager)
			.reader(todoItemReader)
			.processor(todoItemProcessor)
			.writer(todoItemWriter)
			.taskExecutor(taskExecutor())
			.build();
	}

	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("weather-notification-");
		asyncTaskExecutor.setConcurrencyLimit(10);
		return asyncTaskExecutor;
	}
	@Scheduled(cron = "0 0 9 * * ?")
	public void runWeatherNotificationJob() {
		try {
			JobParameters jobParameters = new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();
			jobLauncher.run(weatherNotificationJob(), jobParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
