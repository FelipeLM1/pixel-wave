package com.pixelwave.colorservice.service;

import com.pixelwave.colorservice.model.ColorFilterEnum;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
public class ColorImageService {

    public Resource applyFilter(Long imageId, ColorFilterEnum filter) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        var inputImage = new File("uploads/teste.jpeg");
        Thumbnails
                .of(inputImage)
                .scale(1.0)
                .addFilter(filter.getFilter())
                .toOutputStream(outputStream);

        return new ByteArrayResource(outputStream.toByteArray());
    }


}
