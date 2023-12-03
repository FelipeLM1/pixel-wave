package com.pixelwave.imagestorage.service;

import com.pixelwave.imagestorage.model.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    Image uploadImage(String name, MultipartFile file) throws IOException;

    Image getImageById(Long id);

    List<Image> getAllImages();

    void deleteImage(Long id);

    Resource loadImageAsResource(String id) throws IOException;
}
