package com.pixelwave.imagestorage.service.consumer;

import com.pixelwave.imagestorage.dto.ProcessImageDTO;
import com.pixelwave.imagestorage.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Consumer;

@AllArgsConstructor
@Slf4j
@Component
public class SaveProcessedImageConsumer {

    private final ImageService service;

    @Bean
    Consumer<ProcessImageDTO> saveProcessedImage() {
        return processImageDTO -> {
            try {
                log.info("Storage: Updating processed image...");
                service.updateImage(processImageDTO);
                log.info("Storage: Image updated successfully");
            } catch (IOException e) {
                throw new RuntimeException("Error during update image");
            }
        };
    }
}