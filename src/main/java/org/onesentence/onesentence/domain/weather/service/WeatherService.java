package org.onesentence.onesentence.domain.weather.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.onesentence.onesentence.domain.fcm.dto.FCMSendDto;
import org.onesentence.onesentence.domain.fcm.dto.FCMWeatherDto;
import org.onesentence.onesentence.domain.fcm.service.FCMService;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.repository.TodoJpaRepository;
import org.onesentence.onesentence.domain.todo.service.TodoService;
import org.onesentence.onesentence.domain.user.entity.User;
import org.onesentence.onesentence.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

	private final TodoJpaRepository todoJpaRepository;
	private final FCMService fcmService;
	private final UserService userService;
	private final TodoService todoService;

	@Value("${weather.api.key}")
	private String apiKey;

	@Value("${weather.api.url}")
	private String apiUrl;

	// 날짜를 yyyyMMdd 형식으로 변환하는 메서드
	private String getFormattedDate(LocalDateTime dateTime) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return dateTime.format(dateFormatter);
	}

	// 시간의 분을 HHmm 형식으로 변환하고 필요한 경우 시간을 조정하는 메서드
	private String getFormattedHour(LocalDateTime dateTime) {
		DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HHmm");
		String formattedTime = dateTime.format(hourFormatter);
		int minute = Integer.parseInt(formattedTime.substring(2));
		int hour = Integer.parseInt(formattedTime.substring(0, 2));

		// 분이 45 미만이면 이전 시간의 30분으로 설정
		if (minute < 45) {
			if (hour == 0) {
				return "2330";
			} else {
				return String.format("%02d30", hour - 1);
			}
		} else {
			return String.format("%02d30", hour);
		}
	}

	public Map<String, String> forecast(LocalDateTime dateTime) throws Exception {
		RestTemplate restTemplate = new RestTemplate();

		String stringUrl = String.format(
			"%s?serviceKey=%s&pageNo=1&numOfRows=1000&dataType=XML&base_date=%s&base_time=%s&nx=55&ny=127",
			apiUrl, apiKey, getFormattedDate(dateTime), getFormattedHour(dateTime));

		URI uri = new URI(stringUrl);

		String response = restTemplate.getForObject(uri, String.class);

		log.info(response);

		// XML -> Map 변환
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new java.io.ByteArrayInputStream(response.getBytes()));
		NodeList items = doc.getElementsByTagName("item");

		Map<String, String> weatherData = new HashMap<>();
		for (int i = 0; i < items.getLength(); i++) {
			var item = items.item(i);
			var categoryNode = item.getChildNodes().item(2);
			var obsrValueNode = item.getChildNodes().item(5);

			// Check for null and print debug information if necessary
			if (categoryNode == null || obsrValueNode == null) {
				System.out.println("Null node found at index: " + i);
				continue;
			}

			String category = categoryNode.getTextContent();
			String obsrValue = obsrValueNode.getTextContent();

			switch (category) {
				case "T1H":
					weatherData.put("tmp", obsrValue);
					break;
				case "REH":
					weatherData.put("hum", obsrValue);
					break;
				case "SKY":
					weatherData.put("sky", obsrValue);
					break;
				case "PTY":
					weatherData.put("sky2", obsrValue);
					break;
			}
		}

		return weatherData;
	}

	public String procWeather(LocalDateTime dateTime) throws Exception {

		Map<String, String> dictSky = forecast(dateTime);

		// 기본 날씨 메시지 설정
		StringBuilder strSky = new StringBuilder();

		// 날씨 상태에 따른 메시지 추가
		if (dictSky != null) {
			String sky2 = dictSky.get("sky2");

			// sky2 (강수형태) 처리
			if (sky2 != null) {

				switch (sky2) {
					case "0":
						strSky.append("0");
						break;
					case "1":
						strSky.append("1");
						break;
					case "2":
						strSky.append("2");
						break;
					case "3":
						strSky.append("3");
						break;
					case "5":
						strSky.append("5");
						break;
					case "6":
						strSky.append("6");
						break;
					case "7":
						strSky.append("7");
						break;
				}

			}

		}

		return strSky.toString();
	}

	public FCMWeatherDto test(Long todoId) throws FirebaseMessagingException {

		Todo todo = todoService.findById(todoId);
		User user = userService.findByUserId(todo.getUserId());

		String recommendDate = todoService.findRecommendedTimeSlot(todo);

		FCMWeatherDto fcmWeatherDto = FCMWeatherDto.builder()
			.token(user.getFcmToken())
			.title(todoService.dateConvertToString(todo.getStart()) + "에 비가 올 예정입니다.")
			.body("일정 변경을 원하시면 클릭하세요!")
			.type("weather")
			.date(recommendDate)
			.todoTitle(todo.getTitle())
			.todoId(todo.getId())
			.build();

		log.info("추천 날짜 : " + recommendDate);
		fcmService.sendWeatherPushTo(fcmWeatherDto);

		return fcmWeatherDto;
	}


}
