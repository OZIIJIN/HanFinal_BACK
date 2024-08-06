package org.onesentence.onesentence.domain.fcm.job;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationContext;
import org.onesentence.onesentence.domain.fcm.dto.FCMSendDto;
import org.onesentence.onesentence.domain.fcm.service.FCMService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Component
public class FcmJob implements Job {

	@Autowired
	private FCMService fcmService;

	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		String todoTitle = jobExecutionContext.getMergedJobDataMap().getString("todoTitle");

		FCMSendDto fcmSendDto = FCMSendDto.builder()
			.token(jobExecutionContext.getMergedJobDataMap().getString("fcmToken"))
			.title(todoTitle + "시작할 시간입니다!")
			.body("시작하셨다면 진행 사항을 변경하세요!")
			.build();

		fcmService.test(todoTitle);


	}
}
