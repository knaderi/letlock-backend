/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
public class LetlockFiletransferBackendApplication {
 /*
   @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*", "http://localhost:3000",
                        "http://letlockweb-dev.s3-website-us-west-2.amazonaws.com", "http://letlockbackenddev.us-west-2.elasticbeanstalk.com",
                        "http://letlockweb-qa.s3-website-us-west-2.amazonaws.com", "http://letlockbackend-qa-1.us-west-2.elasticbeanstalk.com");

            }
        };
    }

*/   
   
   @SuppressWarnings("rawtypes")
@Bean
   public FilterRegistrationBean corsFilter() {
       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
       CorsConfiguration config = new CorsConfiguration();
       config.setAllowCredentials(false);
       //ToDo: Investigate if we can restrict allowed origins. Current concern is the desktop app calls.
       config.addAllowedOrigin("*");
       config.addAllowedHeader("*");
       config.addAllowedMethod("*");
       source.registerCorsConfiguration("/**", config);
       @SuppressWarnings("unchecked")
       FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
       //Setting order to 0 so it will precede AuthenticationFilter
       bean.setOrder(0);
       return bean;
   }
   
    public static void main(String[] args) {
        SpringApplication.run(LetlockFiletransferBackendApplication.class, args);
    }

}
