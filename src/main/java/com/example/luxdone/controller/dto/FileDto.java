package com.example.luxdone.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Getter
public class FileDto {
    private byte[] data;
    private MetadataDto metadataDto;
}
