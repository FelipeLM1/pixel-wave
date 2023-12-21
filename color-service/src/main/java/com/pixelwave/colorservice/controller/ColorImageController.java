package com.pixelwave.colorservice.controller;

import com.pixelwave.colorservice.model.ColorFilterEnum;
import com.pixelwave.colorservice.model.ImageFilterRequest;
import com.pixelwave.colorservice.service.ColorImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/v1/color")
public class ColorImageController {

    private final ColorImageService service;

    public ColorImageController(ColorImageService service) {
        this.service = service;
    }

    @PostMapping("/apply-filter")
    public ResponseEntity<Resource> applyColorFilter(@RequestBody ImageFilterRequest imageRequest) throws IOException {
        ColorFilterEnum selectedFilter = ColorFilterEnum.fromString(imageRequest.filter());
        var res = service.applyFilter(imageRequest.id(), selectedFilter);

        if (Objects.isNull(res)) return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        return ResponseEntity.ok()
                .headers(getHttpHeaders(res.filename()))
                .contentLength(res.resource().contentLength())
                .body(res.resource());
    }

    private static HttpHeaders getHttpHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "color_filter_" + filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return headers;
    }
}
