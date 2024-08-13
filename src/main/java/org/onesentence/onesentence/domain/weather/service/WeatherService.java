package org.onesentence.onesentence.domain.weather.service;

import com.google.api.client.util.Value;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.onesentence.onesentence.domain.weather.dto.WeatherDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;

@Service
public class WeatherService {

	@Value("${weather.api.key}")
	private String apiKey;

	@Value("${weather.api.url}")
	private String apiUrl;

	private final Map<String, String> intToWeather = new HashMap<String, String>() {{
		put("0", "맑음");
		put("1", "비");
		put("2", "비/눈");
		put("3", "눈");
		put("5", "빗방울");
		put("6", "빗방울눈날림");
		put("7", "눈날림");
	}};

	public WeatherDto getWeather() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String url = buildUrl();
		String response = restTemplate.getForObject(url, String.class);

		// XML 파싱
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(response)));
		document.getDocumentElement().normalize();

		String temperature = document.getElementsByTagName("obsrValue").item(0).getTextContent();
		String skyConditionCode = document.getElementsByTagName("obsrValue").item(1).getTextContent();
		String skyCondition = intToWeather.get(skyConditionCode);

		return new WeatherDto(temperature, skyCondition);
	}

	private String buildUrl() {
		String baseDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String baseTime = new SimpleDateFormat("HHmm").format(new Date());

		return String.format(apiUrl, apiKey, baseDate, baseTime, "55", "127");
	}
}
