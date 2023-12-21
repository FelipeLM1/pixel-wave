package com.pixelwave.colorservice.dto;

import org.springframework.core.io.Resource;

public record ResourceResponseDTO(
        String filename,
        Resource resource
) {
}
