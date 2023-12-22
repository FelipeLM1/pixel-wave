package com.pixelwave.imagestorage.dto;

public record ProcessImageParams(
        String name,
        String filter,
        Double scale,
        String format
) {
}
