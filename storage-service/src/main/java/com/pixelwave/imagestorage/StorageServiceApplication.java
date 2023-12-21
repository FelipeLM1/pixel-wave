package com.pixelwave.imagestorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PixelWaveImageStorageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PixelWaveImageStorageServiceApplication.class, args);
    }

}