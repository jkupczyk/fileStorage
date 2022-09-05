package com.example.luxdone.scheduler;

import com.example.luxdone.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class FileStateScheduler {

    private final FileService fileService;

    @Scheduled(fixedDelayString = "${processFilesFixedDelay}")
    public void processFilesJob() {
        final int count = fileService.uploadFiles();
        log.info("Changed {} files to READY state", count);
    }

    @Scheduled(fixedDelayString = "${removeFilesFixedDelay}")
    public void removeFilesJob() {
        fileService.deleteFilesByState();
        log.info("Remove files job finished deleted");
    }
}
