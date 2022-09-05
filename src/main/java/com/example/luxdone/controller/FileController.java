package com.example.luxdone.controller;

import com.example.luxdone.controller.dto.FileDto;
import com.example.luxdone.controller.dto.MetadataDto;
import com.example.luxdone.entity.Metadata;
import com.example.luxdone.exception.FileFormatException;
import com.example.luxdone.exception.NotFoundException;
import com.example.luxdone.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID sendFile(@RequestParam("file") final MultipartFile file) throws IOException, FileFormatException {
        return fileService.saveFile(file);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<InputStreamResource> getFile(@PathVariable final UUID fileId) throws NotFoundException, FileFormatException {
        final FileDto fileDto = fileService.readFile(fileId);
        return mapFileToResponseEntity(fileDto);
    }

    @PutMapping("/{fileId}")
    public UUID updateFile(@PathVariable final UUID fileId, @RequestParam("file") final MultipartFile file) throws FileFormatException, IOException, NotFoundException {
        return fileService.updateFile(fileId, file);
    }

    @DeleteMapping("/{fileId}")
    public void deleteFile(@PathVariable final UUID fileId) throws NotFoundException {
        fileService.deleteFile(fileId);
    }

    private ResponseEntity<InputStreamResource> mapFileToResponseEntity(final FileDto fileDto) {
        final MetadataDto metadata = fileDto.getMetadataDto();
        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(fileDto.getMetadataDto().getExtension().getMediaType());
        responseHeaders.set(Metadata.Fields.name, metadata.getName());
        responseHeaders.set(Metadata.Fields.size, String.valueOf(metadata.getSize()));
        responseHeaders.set(Metadata.Fields.extension, String.valueOf(metadata.getExtension()));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new InputStreamResource(new ByteArrayInputStream(fileDto.getData())));
    }
}
