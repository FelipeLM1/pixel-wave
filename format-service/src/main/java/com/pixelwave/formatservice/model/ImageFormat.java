package com.pixelwave.formatservice.model;

public enum ImageFormat {
    JPEG("jpeg"),
    PNG("png"),
    BMP("bmp"),
    WBMP("wbmp"),
    GIF("gif");

    private final String value;

    ImageFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ImageFormat fromString(String value) {
        for (ImageFormat format : ImageFormat.values()) {
            if (format.value.equalsIgnoreCase(value)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Tipo de imagem não suportado: " + value);
    }
}
