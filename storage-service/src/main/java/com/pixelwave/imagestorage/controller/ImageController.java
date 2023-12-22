package com.pixelwave.imagestorage.controller;

import com.pixelwave.imagestorage.config.exception.ImageNotFoundException;
import com.pixelwave.imagestorage.dto.ProcessImageParams;
import com.pixelwave.imagestorage.model.Image;
import com.pixelwave.imagestorage.service.ImageService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/process", consumes = {"multipart/form-data"})
    public ResponseEntity<Image> processImage(
            @RequestParam("name") String name,
            @RequestParam("file") MultipartFile file,
            @RequestParam("color") String color,
            @RequestParam("scale") Double scale,
            @RequestParam("format") String format
    ) throws IOException {
        log.info("Image processing request received");
        Image savedImage = imageService.processNewImage(new ProcessImageParams(name, color, scale, format), file);
        log.info("Original image saved. Wait a moment while image is processing...");
        return ResponseEntity.ok(savedImage);
    }

    @PostMapping(value = "/process/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> processImage(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("color") String color,
            @RequestParam("scale") Double scale,
            @RequestParam("format") String format
    ) throws IOException {
        log.info("Image processing request received");
        imageService.processImageById(new ProcessImageParams(name, color, scale, format), id);
        log.info("Original image found. Wait a moment while image is processing...");
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long id) throws IOException {
        log.info("Recebida solicitação de download para a imagem com ID: {}", id);
        var image = imageService.getImageById(id);
        return createFileResponse(image, "attachment");
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> viewImage(@PathVariable Long id) throws IOException {
        log.info("Recebida solicitação para visualizar a imagem com ID: {}", id);
        var image = imageService.getImageById(id);
        var res = createFileResponse(image, "inline");
        log.info("Visualização da imagem {} realizada com sucesso", image.getName());
        return res;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Image> getImageById(@PathVariable Long id) {
        log.info("Recebida solicitação para obter a informação da imagem com ID: {}", id);
        Image image = imageService.getImageById(id);
        log.info("Informação da imagem {} obtida com sucesso", image != null ? image.getName() : "N/A");
        return ResponseEntity.ok(image);
    }

    @GetMapping
    public ResponseEntity<List<Image>> getAllImages() {
        log.info("Recebida solicitação para obter a lista de todas as imagens");
        List<Image> images = imageService.getAllImages();
        log.info("Lista de imagens obtida com sucesso");
        return ResponseEntity.ok(images);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        log.info("Recebida solicitação para excluir a imagem com ID: {}", id);
        imageService.deleteImage(id);
        log.info("Imagem excluída com sucesso");
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