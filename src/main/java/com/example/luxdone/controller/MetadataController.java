package com.example.luxdone.controller;

import com.example.luxdone.controller.dto.MetadataDto;
import com.example.luxdone.exception.NotFoundException;
import com.example.luxdone.service.MetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class MetadataController {

    private final MetadataService metadataService;

    @GetMapping("/metadata")
    public List<MetadataDto> getAllMetaData(@RequestParam @Min(0) final int page, @RequestParam @Min(1) final int size) {
        return metadataService.getAllMetaData(page, size);
    }

    @GetMapping("/metadata/{uuid}")
    public MetadataDto getMetaDataByUuid(@PathVariable final UUID uuid) throws NotFoundException {
        return metadataService.getMetaDataByUuid(uuid);
    }
}
