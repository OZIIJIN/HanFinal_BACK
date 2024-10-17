package org.onesentence.onesentence.global.job;

import com.google.firebase.messaging.FirebaseMessagingException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.onesentence.onesentence.domain.fcm.dto.FCMSendDto;
import org.onesentence.onesentence.domain.fcm.service.FCMService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

@Slf4j
public class FcmJob implements Job {

	private FCMService fcmService;

	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		if (fcmService == null) {
			// [STEP1] Service 인터페이스를 호출하기 위해 ApplicationContext에 appContext 이름으로 bean을 등록합니다.
			ApplicationContext appCtx = (ApplicationContext) jobExecutionContext.getJobDetail()
				.getJobDataMap().get("appContext");
			fcmService = appCtx.getBean(FCMService.class);
		}

		String todoTitle = jobExecutionContext.getMergedJobDataMap().getString("todoTitle");

		FCMSendDto fcmSendDto = FCMSendDto.builder()
			.token(jobExecutionContext.getMergedJobDataMap().getString("fcmToken"))
			.title(todoTitle + "시작할 시간입니다!")
			.body("시작하셨다면 진행 사항을 변경하세요!")
			.todoId(jobExecutionContext.getMergedJobDataMap().getLong("todoId"))
			.build();

		try {
			fcmService.sendMessageTo(fcmSendDto);
		} catch (IOException | FirebaseMessagingException e) {
			throw new RuntimeException(e);
		}


	}
}
