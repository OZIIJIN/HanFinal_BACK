package org.onesentence.onesentence.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Slf4j
@Configuration
public class FirebaseConfig {

	@Value("${firebase.key-path}")
	String fcmKeyPath;

	@Bean
	public FirebaseApp firebaseApp() throws IOException {
		System.out.println("FIREBASE_KEY_PATH: " + System.getenv("FIREBASE_KEY_PATH"));

		InputStream refreshToken = new ClassPathResource(fcmKeyPath).getInputStream();

		FirebaseOptions options = FirebaseOptions
			.builder()
			.setCredentials(GoogleCredentials.fromStream(refreshToken))
			.build();
		return FirebaseApp.initializeApp(options);
	}
	@Bean
	public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
		return FirebaseMessaging.getInstance(firebaseApp);
	}


}
