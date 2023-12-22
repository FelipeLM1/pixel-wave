package com.pixelwave.colorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ColorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColorServiceApplication.class, args);
    }

}
