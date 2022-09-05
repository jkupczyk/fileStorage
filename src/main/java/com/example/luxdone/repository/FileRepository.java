package com.example.luxdone.repository;

import com.example.luxdone.entity.File;
import com.example.luxdone.entity.FileState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query(
            "SELECT file FROM File file " +
                    "join fetch file.metadata metadata " +
                    "where file.uuid = :uuid"
    )
    Optional<File> findByUuidWithMetadata(final UUID uuid);

    @Query(
            "SELECT file FROM File file " +
                    "join fetch file.metadata metadata " +
                    "where metadata.fileState = :fileState"
    )
    List<File> findByFileState(final FileState fileState);

    int deleteByMetadataFileState(final FileState fileState);
}
