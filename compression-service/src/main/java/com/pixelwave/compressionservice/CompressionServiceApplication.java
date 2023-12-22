package com.pixelwave.compressionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient

public class CompressionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompressionServiceApplication.class, args);
    }

}
