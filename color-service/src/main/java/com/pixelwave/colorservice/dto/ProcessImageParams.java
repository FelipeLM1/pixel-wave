package com.pixelwave.colorservice.dto;

public record ProcessImageParams(
        String name,
        String filter,
        Double scale,
        String format
) {
}
