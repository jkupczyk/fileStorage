package com.example.luxdone.entity;

import com.example.luxdone.controller.dto.MetadataDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class Metadata {

    @Id
    private Long id;

    private String name;

    private long size;

    @Enumerated(EnumType.STRING)
    private FileType extension;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_state")
    private FileState fileState = FileState.UPLOADING;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    private File file;

    public void updateFor(MultipartFile multipartFile) {
        this.name = multipartFile.getOriginalFilename();
        this.size = multipartFile.getSize();
        this.extension = FileType.getByName(FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        this.fileState = FileState.UPLOADING;
    }

    public MetadataDto mapToDto() {
        return MetadataDto.of(this.file.getUuid(), this.getName(), this.getSize(), this.getFileState(), this.getExtension());
    }

    private Metadata(String name, long size, FileType extension) {
        this.name = name;
        this.size = size;
        this.extension = extension;
    }

    public static Metadata of(MultipartFile file) {
        return new Metadata(file.getOriginalFilename(), file.getSize(), FileType.getByName(FilenameUtils.getExtension(file.getOriginalFilename())));
    }
}
