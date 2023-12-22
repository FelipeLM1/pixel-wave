package com.pixelwave.imagestorage.infra;

import com.pixelwave.imagestorage.config.StorageProperties;
import com.pixelwave.imagestorage.dto.ProcessImageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StorageEventGatewayWithStreamBridge implements StorageEventGateway {

    private final StreamBridge streamBridge;
    private final StorageProperties storageProperties;

    @Override
    public void sendProcessImageEvent(ProcessImageDTO dto) {
        log.info("Processing request made. Image saved. Id: {}, Name: {}", dto.idImage(), dto.params().name());
        streamBridge.send(storageProperties.getImageCreatedChannel(), dto);
    }
}
