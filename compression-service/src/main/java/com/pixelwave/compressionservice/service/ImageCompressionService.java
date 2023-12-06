package com.pixelwave.compressionservice.service;

import com.pixelwave.compressionservice.dto.CompressImageDTO;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
public class ImageCompressionService {
    public Resource compressImage(CompressImageDTO compressImageDTO) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //TODO pegar a imagem do servico de storage
        var inputImage = new File("uploads/teste.jpeg");

        try {
            Thumbnails
                    .of(inputImage)
                    .outputQuality(compressImageDTO.compressionQuality())
                    .scale(1.0)
                    .toOutputStream(outputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return createTempFile(outputStream.toByteArray());
    }

    private Resource createTempFile(byte[] byteArray) {
        return new ByteArrayResource(byteArray);
    }
}

