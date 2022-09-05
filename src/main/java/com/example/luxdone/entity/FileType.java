package com.example.luxdone.entity;

import com.example.luxdone.exception.FileFormatException;
import lombok.Getter;
import org.springframework.http.MediaType;

import java.util.Arrays;

@Getter
public enum FileType {
    JPG(MediaType.IMAGE_JPEG),
    HTML(MediaType.TEXT_HTML),
    JSON(MediaType.APPLICATION_JSON);

    final private MediaType mediaType;

    FileType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public static FileType getByName(String extension) {
        return Arrays.stream(FileType.values())
                .filter(it -> it.name().equalsIgnoreCase(extension))
                .findFirst()
                .orElseThrow(() -> new FileFormatException("File extension %s not supported", extension));
    }
}