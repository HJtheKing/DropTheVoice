package com.ssafy.a505;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DropTheVoiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DropTheVoiceApplication.class, args);
	}

}
