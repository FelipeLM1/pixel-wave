package com.pixelwave.imagestorage.service;

import com.pixelwave.imagestorage.config.ImageStorageProperties;
import com.pixelwave.imagestorage.config.exception.ImageNotFoundException;
import com.pixelwave.imagestorage.config.exception.ImageStorageException;
import com.pixelwave.imagestorage.config.exception.InvalidImageException;
import com.pixelwave.imagestorage.dto.ProcessImageDTO;
import com.pixelwave.imagestorage.dto.ProcessImageParams;
import com.pixelwave.imagestorage.infra.StorageEventGateway;
import com.pixelwave.imagestorage.model.Image;
import com.pixelwave.imagestorage.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final ImageStorageProperties storageProperties;
    private final ImageRepository imageRepository;
    private final StorageEventGateway storageEventGateway;

    @Override
    public Image processNewImage(ProcessImageParams params, MultipartFile file) {
        try {
            if (!isImage(file)) {
                throw new InvalidImageException("O arquivo não é uma imagem válida.");
            }

            var fileExtension = file.getContentType().split("/")[1];
            String uniqueId = UUID.randomUUID().toString();
            var nameFile = String.format("%s_%s.%s", params.name(), uniqueId, fileExtension);
            Path filePath = getFilePath(nameFile);

            storageSystemImage(filePath, file.getInputStream());

            Image image = Image.builder()
                    .name(params.name())
                    .contentType(file.getContentType())
                    .path(filePath.toString())
                    .build();

            var imageSaved = imageRepository.save(image);

            storageEventGateway.sendProcessImageEvent(new ProcessImageDTO(
                    file.getBytes(),
                    imageSaved.getId(),
                    params
            ));

            return imageSaved;

        } catch (IOException e) {
            handleImageStorageException("Falha ao salvar a imagem.", e);
            return null;
        }
    }

    @Override
    public void processImageById(ProcessImageParams params, Long id) throws IOException {

        var image = getImageById(id);
        Path path = Path.of(image.getPath());
        byte[] byteArray = Files.readAllBytes(path);
        log.info("the image with id {} will be processed", id);
        storageEventGateway.sendProcessImageEvent(new ProcessImageDTO(
                byteArray,
                id,
                params
        ));
    }

    private void storageSystemImage(Path filePath, InputStream inputStream) throws IOException {
        Path directory = Path.of(storageProperties.getDirectory());
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    Path getFilePath(String nameFile) {
        Path directory = Path.of(storageProperties.getDirectory());
        return directory.resolve(nameFile);
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
    public void updateImage(ProcessImageDTO imageDTO) throws IOException {

        var imageDb = getImageById(imageDTO.idImage());
        Path path = Path.of(imageDb.getPath());
        storageSystemImage(path, new ByteArrayInputStream(imageDTO.imageBytes()));

        imageRepository.save(new Image(
                imageDTO.idImage(),
                imageDTO.params().name(),
                new Tika().detect(imageDTO.imageBytes()),
                imageDb.getPath()
        ));

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
        log.error(message, cause);
        throw new ImageStorageException(message, cause);
    }
}


