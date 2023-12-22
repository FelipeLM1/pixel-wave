package com.pixelwave.colorservice.service;

import com.pixelwave.colorservice.dto.ProcessImageDTO;
import com.pixelwave.colorservice.model.ColorFilterEnum;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Function;

@Slf4j
@Component
public class ColorImageFunction {

    @Bean
    public Function<ProcessImageDTO, ProcessImageDTO> applyColorFilter() {
        return (imageDTO -> {
            log.info("Color Filter Function: Image processing...");

            var outputStream = new ByteArrayOutputStream();

            try {
                Thumbnails
                        .of(new ByteArrayInputStream(imageDTO.imageBytes()))
                        .scale(1.0)
                        .addFilter(ColorFilterEnum.fromString(imageDTO.params().filter()).getFilter())
                        .toOutputStream(outputStream);

            } catch (IOException e) {
                throw new RuntimeException("IO Error! Error processing image", e);
            }

            log.info("Color Filter Function: Image processing completed. id: {}, Filter: {}",
                    imageDTO.idImage(), imageDTO.params().filter());

            return new ProcessImageDTO(outputStream.toByteArray(), imageDTO.idImage(), imageDTO.params());
        });
    }
}