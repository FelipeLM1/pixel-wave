package com.pixelwave.compressionservice.controller;

import com.pixelwave.compressionservice.dto.CompressImageDTO;
import com.pixelwave.compressionservice.service.ImageCompressionService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/compress")
public class ImageCompressionController {

    private final ImageCompressionService compressionService;

    public ImageCompressionController(ImageCompressionService compressionService) {
        this.compressionService = compressionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> compressImage(@PathVariable Long id, @RequestParam(name = "quality") Double quality) throws IOException {
        HttpHeaders headers = getHttpHeaders();

        var resource = compressionService.compressImage(new CompressImageDTO(id, quality));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "compressed_image.jpg");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return headers;
    }
}

