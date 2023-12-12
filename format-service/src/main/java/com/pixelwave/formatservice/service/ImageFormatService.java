package com.pixelwave.formatservice.service;

import com.pixelwave.formatservice.model.ImageFormat;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
public class ImageFormatService {

    public Resource formatImage(Long id, String format) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        var inputImage = new File("uploads/teste.jpeg");
        var formatToCovert = ImageFormat.fromString(format);
        try {
            Thumbnails.of(inputImage)
                    .outputFormat(formatToCovert.getValue())
                    .scale(1.0)
                    .toOutputStream(outputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ByteArrayResource(outputStream.toByteArray());
    }

}
