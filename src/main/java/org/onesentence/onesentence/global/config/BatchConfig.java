package org.onesentence.onesentence.global.config;

import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.global.WeatherTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class BatchConfig {

	private final WeatherTasklet weatherTasklet;
	private final JobLauncher jobLauncher;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job weatherNotificationJob() {
		return new JobBuilder("weatherNotificationJob", jobRepository)
			.start(weatherNotificationStep())
			.incrementer(new RunIdIncrementer())
			.build();
	}

	@Bean
	public Step weatherNotificationStep() {
		return new StepBuilder("weatherNotificationStep", jobRepository)
			.tasklet(weatherTasklet, transactionManager)
			.build();
	}

	@Scheduled(cron = "0 0 7 * * ?")  // 매일 오전 7시에 실행
	public void runWeatherNotificationJob() {
		try {
			JobParameters jobParameters = new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())  // Unique parameter to rerun the job
				.toJobParameters();

			jobLauncher.run(weatherNotificationJob(), jobParameters);
		} catch (Exception e) {
			e.printStackTrace();  // 오류 로그를 출력합니다.
		}
	}
}
