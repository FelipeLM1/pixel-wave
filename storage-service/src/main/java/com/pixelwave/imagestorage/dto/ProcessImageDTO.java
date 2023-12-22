package com.pixelwave.imagestorage.dto;

import java.util.Arrays;
import java.util.Objects;

public record ProcessImageDTO(
        byte[] imageBytes,
        Long idImage,
        ProcessImageParams params
) {

    @Override
    public String toString() {
        return "ProcessImageDTO{" +
                "idImage=" + idImage +
                ", params=" + params +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessImageDTO that = (ProcessImageDTO) o;
        return Arrays.equals(imageBytes, that.imageBytes) && Objects.equals(idImage, that.idImage) && Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idImage, params);
        result = 31 * result + Arrays.hashCode(imageBytes);
        return result;
    }
}
