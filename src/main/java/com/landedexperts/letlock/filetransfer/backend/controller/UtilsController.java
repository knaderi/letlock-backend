package com.landedexperts.letlock.filetransfer.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.OrderMapper;


@RestController
public class UtilsController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    OrderMapper orderMapper;
    
    @GetMapping(value = "/health")
    public ResponseEntity<String> healthCheck() throws Exception {
        HttpStatus status = HttpStatus.OK;
        String body = "OK";
        try {
            String value = orderMapper.getPackages(false, false);
        } catch(Exception e) {
            logger.info("DB connection check error: " + e.toString());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body = "DB connection error";
        }
        return ResponseEntity.status(status).body(body);
    }
}
