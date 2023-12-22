package com.pixelwave.compressionservice.service;

import com.pixelwave.compressionservice.dto.CompressImageDTO;
import com.pixelwave.compressionservice.dto.ResourceResponseDTO;
import com.pixelwave.compressionservice.service.request.StorageClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class ImageCompressionService {

    private static final Logger logger = LoggerFactory.getLogger(ImageCompressionService.class);

    private final StorageClient storageClient;

    public ImageCompressionService(StorageClient storageClient) {
        this.storageClient = storageClient;
    }

    @Retry(name = "storage-service", fallbackMethod = "fallbackRetryApplyFilter")
    @CircuitBreaker(name = "storage-service", fallbackMethod = "fallbackCircuitBreakerApplyFilter")
    @RateLimiter(name = "storage-service", fallbackMethod = "fallbackRateLimiterApplyFilter")
    @Bulkhead(name = "storage-service", fallbackMethod = "fallbackBulkheadApplyFilter", type = Bulkhead.Type.THREADPOOL)
    public ResourceResponseDTO compressImage(CompressImageDTO compressImageDTO) throws IOException {

        var imageRes = storageClient.findImageById(compressImageDTO.id()).getBody();

        if (Objects.isNull(imageRes)) {
            throw new IllegalArgumentException("Erro ao encontrar imagem requisitada.");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails
                .of(imageRes.getInputStream())
                .scale(compressImageDTO.scale())
                .toOutputStream(outputStream);

        return new ResourceResponseDTO(imageRes.getFilename(), createTempFile(outputStream.toByteArray()));
    }

    private Resource createTempFile(byte[] byteArray) {
        return new ByteArrayResource(byteArray);
    }

    private ResourceResponseDTO fallbackCircuitBreakerApplyFilter(CompressImageDTO dto, Throwable throwable) {
        logger.error("CircuitBreaker Fallback - Falha ao processar a requisição: id: {}, quality: {}", dto.id(), dto.scale());
        logger.error("Fallback acionado devido a uma exceção: {}", throwable.getMessage());
        return null;
    }

    private ResourceResponseDTO fallbackBulkheadApplyFilter(CompressImageDTO dto, Throwable throwable) {
        logger.error("Bulkhead Fallback - Falha ao processar a requisição: id: {}, quality: {}", dto.id(), dto.scale());
        logger.error("Fallback acionado devido a uma exceção: {}", throwable.getMessage());
        return null;
    }

    private ResourceResponseDTO fallbackRetryApplyFilter(CompressImageDTO dto, Throwable throwable) {
        logger.error("Retry Fallback - Falha ao processar a requisição: id: {}, quality: {}", dto.id(), dto.scale());
        logger.error("Fallback acionado devido a uma exceção: {}", throwable.getMessage());
        return null;
    }

    private ResourceResponseDTO fallbackRateLimiterApplyFilter(CompressImageDTO dto, Throwable throwable) {
        logger.error("RateLimiter Fallback - Falha ao processar a requisição: id: {}, quality: {}", dto.id(), dto.scale());
        logger.error("Fallback acionado devido a uma exceção: {}", throwable.getMessage());
        return null;
    }
}

