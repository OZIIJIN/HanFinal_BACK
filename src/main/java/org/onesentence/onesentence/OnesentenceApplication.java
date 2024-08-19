package org.onesentence.onesentence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OnesentenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnesentenceApplication.class, args);
	}

}
