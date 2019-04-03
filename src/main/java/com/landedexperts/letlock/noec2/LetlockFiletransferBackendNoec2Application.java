package com.landedexperts.letlock.noec2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.landedexperts.letlock.noec2.session.SessionManager;

@SpringBootApplication
public class LetlockFiletransferBackendNoec2Application {

	public static void main(String[] args) {
		SpringApplication.run(LetlockFiletransferBackendNoec2Application.class, args);

		SessionManager.initialize();
	}
}
