package com.pixelwave.compressionservice.service;

import com.pixelwave.compressionservice.dto.ProcessImageDTO;
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
public class CompressionFunction {

    @Bean
    public Function<ProcessImageDTO, ProcessImageDTO> compressImage() {
        return (processImageDTO -> {

            log.info("Compression Function: Image processing...");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                Thumbnails
                        .of(new ByteArrayInputStream(processImageDTO.imageBytes()))
                        .scale(processImageDTO.params().scale())
                        .toOutputStream(outputStream);

            } catch (IOException e) {
                throw new RuntimeException("IO Error!", e);
            }
            log.info("Compression Function: Image processing completed. Id: {}, Scale : {}",
                    processImageDTO.idImage(), processImageDTO.params().scale());
            return new ProcessImageDTO(outputStream.toByteArray(), processImageDTO.idImage(), processImageDTO.params());
        });
    }
}
