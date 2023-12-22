package com.pixelwave.compressionservice.controller;

import com.pixelwave.compressionservice.dto.CompressImageDTO;
import com.pixelwave.compressionservice.service.ImageCompressionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/v1/compress")
public class ImageCompressionController {

    private static final Logger logger = LoggerFactory.getLogger(ImageCompressionController.class);

    private final ImageCompressionService compressionService;

    public ImageCompressionController(ImageCompressionService compressionService) {
        this.compressionService = compressionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> compressImage(@PathVariable Long id, @RequestParam(name = "scale") Double scale) throws IOException {
        logger.info("Compress Request: Id: {},  Scale: {}", id, scale);
        var res = compressionService.compressImage(new CompressImageDTO(id, scale));
        HttpHeaders headers = getHttpHeaders(res.filename());

        if (Objects.isNull(res)) return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        logger.info("Processing completed successfully!");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(res.resource().contentLength())
                .body(res.resource());
    }

    private static HttpHeaders getHttpHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "compressed_image_" + filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return headers;
    }
}

