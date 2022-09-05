package com.example.luxdone.service;

import com.example.luxdone.controller.dto.FileDto;
import com.example.luxdone.entity.File;
import com.example.luxdone.entity.FileState;
import com.example.luxdone.entity.FileType;
import com.example.luxdone.exception.FileFormatException;
import com.example.luxdone.exception.NotFoundException;
import com.example.luxdone.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileRepository fileRepository;

    public UUID saveFile(final MultipartFile multipartFile) throws IOException, FileFormatException {
        final File fileEntity = File.of(multipartFile);
        return fileRepository.save(fileEntity).getUuid();
    }

    @Transactional
    public UUID updateFile(final UUID uuid, final MultipartFile file) throws IOException, FileFormatException, NotFoundException {
        final File persistedFile = getPersistedFileWithMetadata(uuid);
        final FileState fileState = persistedFile.getMetadata().getFileState();
        checkIsFileStateSuitable(fileState);
        final boolean isExtensionValid = persistedFile.getMetadata().getExtension()
                .equals(FileType.getByName(FilenameUtils.getExtension(file.getOriginalFilename())));
        if (isExtensionValid) {
            persistedFile.updateState(file);
            return persistedFile.getUuid();
        } else {
            throw new FileFormatException("Couldn't update file with uuid: %s, incompatible file format", uuid);
        }
    }

    public FileDto readFile(final UUID uuid) throws NotFoundException, FileFormatException {
        final File persistedFile = getPersistedFileWithMetadata(uuid);
        final FileState fileState = persistedFile.getMetadata().getFileState();
        checkIsFileStateSuitable(fileState);
        return FileDto.of(persistedFile.getData(), persistedFile.getMetadata().mapToDto());
    }

    @Transactional
    public void deleteFile(final UUID uuid) throws NotFoundException {
        final File persistedFile = getPersistedFileWithMetadata(uuid);
        checkIsFileStateSuitable(persistedFile.getMetadata().getFileState());
        persistedFile.getMetadata().setFileState(FileState.TO_BE_DELETED);
    }

    private void checkIsFileStateSuitable(final FileState fileState) throws FileFormatException {
        if (fileState != FileState.READY) {
            throw new FileFormatException("File state %s is not suitable for this operation", fileState);
        }
    }

    private File getPersistedFileWithMetadata(final UUID uuid) throws NotFoundException {
        return fileRepository.findByUuidWithMetadata(uuid).orElseThrow(() -> new NotFoundException("File with uuid: %s not found", uuid));
    }

    @Transactional
    public int uploadFiles() {
        return fileRepository.findByFileState(FileState.UPLOADING).stream()
                .map(File::getMetadata)
                .peek(it -> it.setFileState(FileState.READY))
                .collect(Collectors.toList())
                .size();
    }

    @Transactional
    public void deleteFilesByState() {
        fileRepository.deleteByMetadataFileState(FileState.TO_BE_DELETED);
    }
}
