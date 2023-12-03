package com.pixelwave.imagestorage.controller;

import com.pixelwave.imagestorage.config.exception.ImageNotFoundException;
import com.pixelwave.imagestorage.model.Image;
import com.pixelwave.imagestorage.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadImage(
            @RequestParam("name") String name,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        logger.info("Recebida solicitação de upload de imagem: {}", name);
        Image savedImage = imageService.uploadImage(name, file);
        logger.info("Imagem {} salva com sucesso", savedImage.getName());
        return ResponseEntity.ok(savedImage);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long id) throws IOException {
        logger.info("Recebida solicitação de download para a imagem com ID: {}", id);
        var image = imageService.getImageById(id);
        return createFileResponse(image, "attachment");
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> viewImage(@PathVariable Long id) throws IOException {
        logger.info("Recebida solicitação para visualizar a imagem com ID: {}", id);
        var image = imageService.getImageById(id);
        var res = createFileResponse(image, "inline");
        logger.info("Visualização da imagem {} realizada com sucesso", image.getName());
        return res;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImageById(@PathVariable Long id) {
        logger.info("Recebida solicitação para obter a informação da imagem com ID: {}", id);
        Image image = imageService.getImageById(id);
        logger.info("Informação da imagem {} obtida com sucesso", image != null ? image.getName() : "N/A");
        return ResponseEntity.ok(image);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Image>> getAllImages() {
        logger.info("Recebida solicitação para obter a lista de todas as imagens");
        List<Image> images = imageService.getAllImages();
        logger.info("Lista de imagens obtida com sucesso");
        return ResponseEntity.ok(images);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        logger.info("Recebida solicitação para excluir a imagem com ID: {}", id);
        imageService.deleteImage(id);
        logger.info("Imagem excluída com sucesso");
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Resource> createFileResponse(Image image, String contentDisposition) throws IOException {

        Resource resource = imageService.loadImageAsResource(image.getPath());
        if (resource == null) {
            throw new ImageNotFoundException(image.getId());
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition + "; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}