package com.pixelwave.imagestorage.config.exception;

import jakarta.persistence.EntityNotFoundException;

public class ImageNotFoundException extends EntityNotFoundException {
    public ImageNotFoundException(Long id) {
        super("Imagem não encontrada com o ID: " + id);
    }
}
