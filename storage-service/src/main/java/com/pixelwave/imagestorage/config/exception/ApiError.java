package com.pixelwave.imagestorage.config.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ApiError {

    private final HttpStatus status;
    private final String message;
    private final String debugMessage;
}

