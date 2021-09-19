package com.flash.operation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author YueLiMin
 * @version 1.0.0
 * @since 11
 */
@SpringBootApplication
@EnableDiscoveryClient
public class OperationApplicationBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(OperationApplicationBootstrap.class, args);
    }
}
