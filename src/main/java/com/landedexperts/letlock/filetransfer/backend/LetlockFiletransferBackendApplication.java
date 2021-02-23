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
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class LetlockFiletransferBackendApplication {
   
   @SuppressWarnings("rawtypes")
   @Bean
   public FilterRegistrationBean corsFilter() {
       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
       CorsConfiguration config = new CorsConfiguration();
       config.setAllowCredentials(false);
       //TODO: Investigate if we can restrict allowed origins. Current concern is the desktop app calls.
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
   
   @Bean
   public CacheManager cacheManager() {
       return new ConcurrentMapCacheManager("installers");
   }
      
    public static void main(String[] args) {
        SpringApplication.run(LetlockFiletransferBackendApplication.class, args);
    }

}
