package org.onesentence.onesentence.global;

import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.fcm.dto.FCMWeatherDto;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.service.TodoService;
import org.onesentence.onesentence.domain.user.entity.User;
import org.onesentence.onesentence.domain.user.service.UserService;
import org.onesentence.onesentence.domain.weather.service.WeatherService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodoItemProcessor implements ItemProcessor<Todo, FCMWeatherDto> {

	private final UserService userService;
	private final WeatherService weatherService;
	private final TodoService todoService;

	@Override
	public FCMWeatherDto process(Todo todo) throws Exception {
		User user = userService.findByUserId(todo.getUserId());
		String weatherForecast = weatherService.procWeather(todo.getStart());

		String today =
			todo.getStart().getMonthValue() + "월 " + todo.getStart().getDayOfMonth() + "일에 ";

		String recommendDate = todoService.findRecommendedTimeSlot(todo);

		String title;

		switch (weatherForecast) {
			case "1" -> title = today + " 비가 올 예정입니다.";
			case "2" -> title = today + " 비와 눈이 올 예정입니다.";
			case "3" -> title = today + " 눈이 올 예정입니다.";
			case "5" -> title = today + " 빗방울이 떨어질 예정입니다.";
			case "6" -> title = today + " 빗방울과 눈이 날릴 예정입니다.";
			case "7" -> title = today + " 눈이 날릴 예정입니다.";
			default -> title = null;
		}

		if (title != null) {
			return FCMWeatherDto.builder()
				.token(user.getFcmToken())
				.title(title)
				.body("일정 변경을 원하시면 클릭하세요!")
				.todoId(todo.getId())
				.type("weather")
				.todoTitle(todo.getTitle())
				.date(recommendDate)
				.build();
		}

		return null;
	}
}

