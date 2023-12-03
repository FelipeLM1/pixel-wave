package com.pixelwave.imagestorage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String contentType;
    private String path;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(id, image.id) && Objects.equals(name, image.name) && Objects.equals(contentType, image.contentType) && Objects.equals(path, image.path);
    }

    public String getFullImageName() {
        return this.name + this.contentType.split("/")[1];
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contentType, path);
    }
}
