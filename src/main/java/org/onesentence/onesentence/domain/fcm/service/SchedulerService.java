package org.onesentence.onesentence.domain.fcm.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.fcm.job.FcmJob;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SchedulerService {

	private final SchedulerFactoryBean schedulerFactoryBean;
	private final ApplicationContext applicationContext;

	private static String APPLICATION_NAME = "appContext";

	public void setScheduler (LocalDateTime start, String fcmToken, String todoTitle, Long todoId)
		throws SchedulerException {

		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(APPLICATION_NAME, applicationContext);
		jobDataMap.put("fcmToken", fcmToken);
		jobDataMap.put("todoTitle", todoTitle);

		JobDetail job = JobBuilder
			.newJob(FcmJob.class)
			.withIdentity("fcmPushJob_" + todoId, "fcmGroup")
			.withDescription("FCM 푸시 알림 Job")
			.setJobData(jobDataMap)
			.build();

		CronTrigger cronTrigger = TriggerBuilder
			.newTrigger()
			.withIdentity("fcmCronTrigger_" + todoId, "fcmGroup")
			.withDescription("FCM 푸시 알림 Trigger")
			.startNow()
			.withSchedule(CronScheduleBuilder.cronSchedule(convertToCron(start)))
			.build();

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		scheduler.start();
		scheduler.scheduleJob(job, cronTrigger);

	}

	private String convertToCron(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ss mm HH dd MM ? yyyy");
		return dateTime.format(formatter);
	}
}
