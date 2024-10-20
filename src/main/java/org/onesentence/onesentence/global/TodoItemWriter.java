package org.onesentence.onesentence.global;

import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.fcm.dto.FCMWeatherDto;
import org.onesentence.onesentence.domain.fcm.service.FCMService;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodoItemWriter implements ItemWriter<FCMWeatherDto> {

	private final FCMService fcmService;

	@Override
	public void write(Chunk<? extends FCMWeatherDto> items) throws Exception {
		for (FCMWeatherDto dto : items) {
			if (dto != null) {
				fcmService.sendWeatherPushTo(dto);
			}
		}
	}
}
