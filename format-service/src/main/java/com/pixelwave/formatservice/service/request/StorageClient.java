package com.pixelwave.formatservice.service.request;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("storage-service")
public interface StorageClient {

    @LoadBalanced
    @GetMapping("/images/download/{id}")
    ResponseEntity<Resource> findImageById(@PathVariable Long id);
}
