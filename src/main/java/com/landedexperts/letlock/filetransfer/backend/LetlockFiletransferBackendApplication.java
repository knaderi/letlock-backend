package com.landedexperts.letlock.filetransfer.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class LetlockFiletransferBackendApplication {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000","http://letlockweb-dev.s3-website-us-west-2.amazonaws.com/");
            }
        };
    }

	public static void main(String[] args) {
		SpringApplication.run(LetlockFiletransferBackendApplication.class, args);
	}
}
