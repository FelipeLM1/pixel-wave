package com.pixelwave.formatservice.dto;

public record ProcessImageParams(
        String name,
        String filter,
        Double scale,
        String format
) {
}
