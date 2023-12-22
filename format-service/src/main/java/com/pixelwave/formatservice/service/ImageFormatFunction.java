package com.pixelwave.formatservice.service;

import com.pixelwave.formatservice.dto.ProcessImageDTO;
import com.pixelwave.formatservice.model.ImageFormat;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Function;


@Component
public class ImageFormatFunction {

    private static final Logger log = LoggerFactory.getLogger(ImageFormatFunction.class);

    @Bean
    Function<ProcessImageDTO, ProcessImageDTO> formatImage() {
        return (imageDTO -> {
            log.info("Color Filter Function: Image processing...");

            var outputStream = new ByteArrayOutputStream();

            try {
                Thumbnails
                        .of(new ByteArrayInputStream(imageDTO.imageBytes()))
                        .outputFormat(ImageFormat.fromString(imageDTO.params().format()).getValue())
                        .scale(1.0)
                        .toOutputStream(outputStream);
            } catch (IOException e) {
                throw new RuntimeException("IO Error! Error processing image", e);
            }

            log.info("Format Function: Image processing completed. id: {}, Format: {}",
                    imageDTO.idImage(), imageDTO.params().format());

            return new ProcessImageDTO(outputStream.toByteArray(), imageDTO.idImage(), imageDTO.params());
        });
    }

}
