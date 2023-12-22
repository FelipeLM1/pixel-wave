package com.pixelwave.imagestorage.config;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class StorageProperties {

    private String imageCreatedChannel = "imageCreatedSupplier-out-0";
}
