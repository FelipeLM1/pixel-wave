package com.pixelwave.compressionservice.controller;

import com.pixelwave.compressionservice.service.ImageCompressionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageCompressionController {

    private final ImageCompressionService compressionService;

    public ImageCompressionController(ImageCompressionService compressionService) {
        this.compressionService = compressionService;
    }

    @PostMapping("/compress")
    public ResponseEntity<byte[]> compressImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "quality", defaultValue = "0.8") double compressionQuality
    ) {
//        try {
//            byte[] compressedImageData = compressionService.compressImage(file.getBytes(), compressionQuality);
//            return ResponseEntity.ok()
//                    .header("Content-Disposition", "attachment; filename=compressed_image.jpg")
//                    .body(compressedImageData);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(null);
//        }
        return null;
    }
}

