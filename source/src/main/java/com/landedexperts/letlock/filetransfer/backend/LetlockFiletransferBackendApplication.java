package com.landedexperts.letlock.filetransfer.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@SpringBootApplication
public class LetlockFiletransferBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LetlockFiletransferBackendApplication.class, args);

		SessionManager.initialize();
	}
}
