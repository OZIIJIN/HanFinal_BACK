package org.onesentence.onesentence.domain.weather.service;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@Slf4j
@Service
public class WeatherService {

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

		String stringUrl = String.format("%s?serviceKey=%s&pageNo=1&numOfRows=1000&dataType=XML&base_date=%s&base_time=%s&nx=55&ny=127",
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

	public boolean procWeather(LocalDateTime dateTime) throws Exception {

		Map<String, String> dictSky = forecast(dateTime);

		// 기본 날씨 메시지 설정
		Boolean needToPush = false;

		// 날씨 상태에 따른 메시지 추가
		if (dictSky != null) {
			String sky2 = dictSky.get("sky2");

			// sky2 (강수형태) 처리
			if (sky2 != null) {
				switch (sky2) {
					case "1":
					case "2":
					case "3":
					case "5":
					case "6":
					case "7":
						needToPush = true;
						break;
				}

			}

		}

		return needToPush;
	}

}
