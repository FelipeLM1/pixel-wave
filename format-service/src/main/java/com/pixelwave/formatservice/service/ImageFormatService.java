package com.pixelwave.formatservice.service;

import com.pixelwave.formatservice.dto.ResourceResponseDTO;
import com.pixelwave.formatservice.model.ImageFormat;
import com.pixelwave.formatservice.service.request.StorageClient;
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
public class ImageFormatService {

    private static final Logger logger = LoggerFactory.getLogger(ImageFormatService.class);

    private final StorageClient storageClient;

    public ImageFormatService(StorageClient storageClient) {
        this.storageClient = storageClient;
    }

    @Retry(name = "storage-service", fallbackMethod = "fallbackRetryApplyFilter")
    @CircuitBreaker(name = "storage-service", fallbackMethod = "fallbackCircuitBreakerApplyFilter")
    @RateLimiter(name = "storage-service", fallbackMethod = "fallbackRateLimiterApplyFilter")
    @Bulkhead(name = "storage-service", fallbackMethod = "fallbackBulkheadApplyFilter", type = Bulkhead.Type.THREADPOOL)
    public ResourceResponseDTO formatImage(Long id, String format) throws IOException {
        var imageRes = storageClient.findImageById(id).getBody();

        if (Objects.isNull(imageRes)) {
            throw new IllegalArgumentException("Erro ao encontrar imagem requisitada.");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        var inputImage = imageRes.getInputStream();
        var formatToCovert = ImageFormat.fromString(format);

        Thumbnails.of(inputImage).outputFormat(formatToCovert.getValue()).scale(1.0).toOutputStream(outputStream);

        return new ResourceResponseDTO(imageRes.getFilename(), new ByteArrayResource(outputStream.toByteArray()));
    }

    private ResourceResponseDTO fallbackCircuitBreakerApplyFilter(Long id, String format, Throwable throwable) {
        logger.error("CircuitBreaker Fallback - Falha ao processar a requisição: id: {}, format: {}", id, format);
        logger.error("Fallback acionado devido a uma exceção: {}", throwable.getMessage());
        return null;
    }

    private ResourceResponseDTO fallbackBulkheadApplyFilter(Long id, String format, Throwable throwable) {
        logger.error("Bulkhead Fallback - Falha ao processar a requisição: id: {}, format: {}", id, format);
        logger.error("Fallback acionado devido a uma exceção: {}", throwable.getMessage());
        return null;
    }

    private ResourceResponseDTO fallbackRetryApplyFilter(Long id, String format, Throwable throwable) {
        logger.error("Retry Fallback - Falha ao processar a requisição: id: {}, format: {}", id, format);
        logger.error("Fallback acionado devido a uma exceção: {}", throwable.getMessage());
        return null;
    }

    private ResourceResponseDTO fallbackRateLimiterApplyFilter(Long id, String format, Throwable throwable) {
        logger.error("RateLimiter Fallback - Falha ao processar a requisição: id: {}, format: {}", id, format);
        logger.error("Fallback acionado devido a uma exceção: {}", throwable.getMessage());
        return null;
    }

}
