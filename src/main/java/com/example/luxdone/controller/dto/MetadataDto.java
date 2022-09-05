package com.example.luxdone.controller.dto;

import com.example.luxdone.entity.FileState;
import com.example.luxdone.entity.FileType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor(staticName = "of")
public class MetadataDto {
    private UUID uuid;
    private String name;
    private long size;
    private FileState fileState;
    private FileType extension;
}
