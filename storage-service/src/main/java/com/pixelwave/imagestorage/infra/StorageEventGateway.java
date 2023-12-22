package com.pixelwave.imagestorage.infra;

import com.pixelwave.imagestorage.dto.ProcessImageDTO;

public interface StorageEventGateway {

    void sendProcessImageEvent(ProcessImageDTO dto);
}
