package com.example.luxdone.service;

import com.example.luxdone.controller.dto.MetadataDto;
import com.example.luxdone.entity.File;
import com.example.luxdone.entity.Metadata;
import com.example.luxdone.exception.NotFoundException;
import com.example.luxdone.repository.MetadataRepository;
import com.example.luxdone.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetadataService {

    private final MetadataRepository metadataRepository;
    private final FileRepository fileRepository;

    public List<MetadataDto> getAllMetaData(final int page, final int size) {
        return metadataRepository.findAll(PageRequest.of(page, size, Sort.by(Metadata.Fields.name))).stream()
                .map(Metadata::mapToDto)
                .collect(Collectors.toList());
    }

    public MetadataDto getMetaDataByUuid(final UUID uuid) throws NotFoundException {
        return fileRepository.findByUuidWithMetadata(uuid)
                .map(File::getMetadata)
                .map(Metadata::mapToDto)
                .orElseThrow(() -> new NotFoundException("Metadata with uuid: %s not found", uuid));
    }
}
