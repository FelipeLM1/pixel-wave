package com.pixelwave.formatservice.controller;

import com.pixelwave.formatservice.model.ImageFormat;
import com.pixelwave.formatservice.service.ImageFormatService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/v1/format")
@Validated
public class FormatController {

    private final ImageFormatService service;

    public FormatController(ImageFormatService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> imageConverter(@PathVariable Long id, @RequestParam(name = "format") String format) throws IOException {
        HttpHeaders headers = getHttpHeaders(ImageFormat.fromString(format).getValue());

        var resource = service.formatImage(id, format);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    private static HttpHeaders getHttpHeaders(String format) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "formated_image." + format);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return headers;
    }
}


