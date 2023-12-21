package com.pixelwave.compressionservice.dto;

import org.springframework.core.io.Resource;

public record ResourceResponseDTO(
        String filename,
        Resource resource
) {
}
