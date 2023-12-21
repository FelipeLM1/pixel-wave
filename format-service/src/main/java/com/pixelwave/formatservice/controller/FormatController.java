package com.pixelwave.formatservice.controller;

import com.pixelwave.formatservice.model.ImageFormat;
import com.pixelwave.formatservice.service.ImageFormatService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

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


        var res = service.formatImage(id, format);

        HttpHeaders headers = getHttpHeaders(res.filename().split("\\.")[0], ImageFormat.fromString(format).getValue());

        if (Objects.isNull(res)) return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(res.resource().contentLength())
                .body(res.resource());
    }

    private static HttpHeaders getHttpHeaders(String filename, String format) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "formated_image_" + filename + "." + format);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return headers;
    }
}


