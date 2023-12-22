package com.pixelwave.imagestorage.config;

import com.pixelwave.imagestorage.config.exception.ApiError;
import com.pixelwave.imagestorage.config.exception.ImageNotFoundException;
import com.pixelwave.imagestorage.config.exception.ImageStorageException;
import com.pixelwave.imagestorage.config.exception.InvalidImageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Erro não tratado: {}", ex.getMessage(), ex);
        return buildResponseEntity(ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("Ocorreu um erro inesperado")
                .debugMessage(ex.getMessage())
                .build());
    }

    @ExceptionHandler(ImageNotFoundException.class)
    protected ResponseEntity<Object> handleImageNotFoundException(ImageNotFoundException ex, WebRequest request) {
        return buildResponseEntity(ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("Imagem não encontrada")
                .debugMessage(ex.getMessage())
                .build());
    }

    @ExceptionHandler(InvalidImageException.class)
    protected ResponseEntity<Object> handleInvalidImageException(InvalidImageException ex, WebRequest request) {
        return buildResponseEntity(ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Arquivo inválido para uma imagem")
                .debugMessage(ex.getMessage())
                .build());
    }

    @ExceptionHandler(ImageStorageException.class)
    protected ResponseEntity<Object> handleImageStorageException(ImageStorageException ex, WebRequest request) {
        return buildResponseEntity(ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("Erro no armazenamento de imagem")
                .debugMessage(ex.getMessage())
                .build());
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}


