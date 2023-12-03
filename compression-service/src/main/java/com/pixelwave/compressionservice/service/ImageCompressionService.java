package com.pixelwave.compressionservice.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ImageCompressionService {
    public void compressImage(File inputImage, File outputImage, double compressionQuality) {
        try {
            Thumbnails
                    .of(inputImage)
                    .outputQuality(compressionQuality)
                    .toFile(outputImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

