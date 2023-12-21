package com.pixelwave.colorservice.service;

import com.pixelwave.colorservice.dto.ResourceResponseDTO;
import com.pixelwave.colorservice.model.ColorFilterEnum;
import com.pixelwave.colorservice.service.request.StorageClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class ColorImageService {

    private static final Logger logger = LoggerFactory.getLogger(ColorImageService.class);

    private final StorageClient storageClient;

    public ColorImageService(StorageClient storageClient) {
        this.storageClient = storageClient;
    }

    @Retry(name = "storage-service", fallbackMethod = "fallbackRetryApplyFilter")
    @CircuitBreaker(name = "storage-service", fallbackMethod = "fallbackCircuitBreakerApplyFilter")
    @RateLimiter(name = "storage-service", fallbackMethod = "fallbackRateLimiterApplyFilter")
    @Bulkhead(name = "storage-service", fallbackMethod = "fallbackBulkheadApplyFilter", type = Bulkhead.Type.THREADPOOL)
    public ResourceResponseDTO applyFilter(Long imageId, ColorFilterEnum filter) throws IOException {
        var imageRes = storageClient.findImageById(imageId).getBody();
        if (Objects.isNull(imageRes)) {
            throw new IllegalArgumentException("Erro ao encontrar imagem requisitada.");
        }

        var outputStream = new ByteArrayOutputStream();

        Thumbnails
                .of(imageRes.getInputStream())
                .scale(1.0)
                .addFilter(filter.getFilter())
                .toOutputStream(outputStream);

        return new ResourceResponseDTO(imageRes.getFilename(), new ByteArrayResource(outputStream.toByteArray()));
    }

    private ResourceResponseDTO fallbackCircuitBreakerApplyFilter(Long imageId, ColorFilterEnum filter, Throwable throwable) {
        logger.error("CircuitBreaker Fallback - Falha ao processar a requisição: id: {}, filter: {}", imageId, filter);
        logger.error("Fallback acionado devido a uma exceção: {}", throwable.getMessage());
        return null;
    }

    private ResourceResponseDTO fallbackBulkheadApplyFilter(Long imageId, ColorFilterEnum filter, Throwable throwable) {
        logger.error("Bulkhead Fallback - Falha ao processar a requisição: id: {}, filter: {}", imageId, filter);
        logger.error("Fallback acionado devido a uma exceção: {}", throwable.getMessage());
        return null;
    }

    private ResourceResponseDTO fallbackRetryApplyFilter(Long imageId, ColorFilterEnum filter, Throwable throwable) {
        logger.error("Retry Fallback - Falha ao processar a requisição: id: {}, filter: {}", imageId, filter);
        logger.error("Fallback acionado devido a uma exceção: {}", throwable.getMessage());
        return null;
    }

    private ResourceResponseDTO fallbackRateLimiterApplyFilter(Long imageId, ColorFilterEnum filter, Throwable throwable) {
        logger.error("RateLimiter Fallback - Falha ao processar a requisição: id: {}, filter: {}", imageId, filter);
        logger.error("Fallback acionado devido a uma exceção: {}", throwable.getMessage());
        return null;
    }

}
