package com.pixelwave.imagestorage.service;

import com.pixelwave.imagestorage.config.ImageStorageProperties;
import com.pixelwave.imagestorage.config.exception.ImageNotFoundException;
import com.pixelwave.imagestorage.config.exception.ImageStorageException;
import com.pixelwave.imagestorage.config.exception.InvalidImageException;
import com.pixelwave.imagestorage.model.Image;
import com.pixelwave.imagestorage.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    private final ImageStorageProperties storageProperties;
    private final ImageRepository imageRepository;

    @Override
    public Image uploadImage(String name, MultipartFile file) {
        try {
            if (!isImage(file)) {
                throw new InvalidImageException("O arquivo não é uma imagem válida.");
            }

            Path directory = Path.of(storageProperties.getDirectory());
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            Path filePath = directory.resolve(Objects.requireNonNull(name.concat("." + file.getContentType().split("/")[1])));

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Image image = Image.builder()
                    .name(name)
                    .contentType(file.getContentType())
                    .path(filePath.toString())
                    .build();

            return imageRepository.save(image);
        } catch (IOException e) {
            handleImageStorageException("Falha ao salvar a imagem.", e);
            return null;
        }
    }

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException(id));
    }

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public void deleteImage(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException(id));

        Path filePath = Paths.get(image.getPath());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            handleImageStorageException("Falha ao excluir a imagem do disco.", e);
        }

        imageRepository.deleteById(id);
    }

    @Override
    public Resource loadImageAsResource(String imagePath) {
        try {
            Path filePath = Paths.get(imagePath);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                handleImageStorageException("Não foi possível ler o arquivo: " + imagePath, null);
            }
        } catch (IOException e) {
            handleImageStorageException("Erro ao carregar a imagem como recurso.", e);
        }
        return null;
    }

    private boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image");
    }

    private void handleImageStorageException(String message, Throwable cause) {
        logger.error(message, cause);
        throw new ImageStorageException(message, cause);
    }
}


