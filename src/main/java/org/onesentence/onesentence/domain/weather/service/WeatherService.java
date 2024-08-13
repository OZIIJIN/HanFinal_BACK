package org.onesentence.onesentence.domain.weather.service;

import com.google.api.client.util.Value;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.onesentence.onesentence.domain.weather.dto.WeatherDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@Slf4j
@Service
public class WeatherService {

		private String apiKey = "G6atz6ur2rR0fmU6O01wKBCj%2BONFqmGx5NZxJy20DS8Phso%2Bge8cdIDXwoqVcD%2BYCKi1QzdBgxMhygq29Gkq4A%3D%3D";

		private String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";

		private String getCurrentDateString() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			return sdf.format(new Date());
		}

		private String getCurrentHourString() {
			SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
			int minute = Integer.parseInt(sdf.format(new Date()).substring(2));
			int hour = Integer.parseInt(sdf.format(new Date()).substring(0, 2));

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

		public Map<String, String> forecast() throws Exception {
			RestTemplate restTemplate = new RestTemplate();

			String stringUrl = String.format("%s?serviceKey=%s&pageNo=1&numOfRows=1000&dataType=XML&base_date=20240813&base_time=0500&nx=55&ny=127",
				apiUrl, apiKey);

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

	public String procWeather() throws Exception {
		Map<String, String> dictSky = forecast();

		// 기본 날씨 메시지 설정
		StringBuilder strSky = new StringBuilder("서울 날씨 : ");

		// 날씨 상태에 따른 메시지 추가
		if (dictSky != null) {
			String sky2 = dictSky.get("sky2");
			String sky = dictSky.get("sky");
			String tmp = dictSky.get("tmp");
			String hum = dictSky.get("hum");

			// sky2 (강수형태) 처리
			if (sky2 != null) {
				switch (sky2) {
					case "0":
						if ("1".equals(sky)) {
							strSky.append("맑음");
						} else if ("3".equals(sky)) {
							strSky.append("구름많음");
						} else if ("4".equals(sky)) {
							strSky.append("흐림");
						} else {
							strSky.append("날씨 정보 없음");
						}
						break;
					case "1":
						strSky.append("비");
						break;
					case "2":
						strSky.append("비와 눈");
						break;
					case "3":
						strSky.append("눈");
						break;
					case "5":
						strSky.append("빗방울이 떨어짐");
						break;
					case "6":
						strSky.append("빗방울과 눈이 날림");
						break;
					case "7":
						strSky.append("눈이 날림");
						break;
					default:
						strSky.append("강수형태 정보 없음");
						break;
				}
			} else {
				strSky.append("강수형태 정보 없음");
			}

			// 온도와 습도 처리
			if (tmp != null) {
				strSky.append("\n온도 : ").append(tmp).append("ºC");
			}
			if (hum != null) {
				strSky.append("\n습도 : ").append(hum).append("%");
			}
		} else {
			strSky.append("날씨 정보 없음");
		}

		return strSky.toString();
	}

}
