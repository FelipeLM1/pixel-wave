package com.pixelwave.imagestorage.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "image.storage")
public class ImageStorageProperties {

    private String directory;

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
