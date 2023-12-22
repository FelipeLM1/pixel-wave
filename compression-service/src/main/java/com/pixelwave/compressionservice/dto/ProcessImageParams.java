package com.pixelwave.compressionservice.dto;

public record ProcessImageParams(
        String name,
        String filter,
        Double scale,
        String format
) {
}
