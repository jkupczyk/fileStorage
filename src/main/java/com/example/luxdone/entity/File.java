package com.example.luxdone.entity;

import com.example.luxdone.exception.FileFormatException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.IOException;
import java.util.UUID;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @EqualsAndHashCode.Include
    private UUID uuid = UUID.randomUUID();

    private byte[] data;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "file")
    private Metadata metadata;

    public void updateState(final MultipartFile file) throws IOException {
        this.setData(file.getBytes());
        this.metadata.updateFor(file);
    }

    public File(final byte[] data, Metadata metadata) {
        this.data = data;
        metadata.setFile(this);
        this.metadata = metadata;
    }

    public static File of(final MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new FileFormatException("File can't be empty");
        }
        return new File(multipartFile.getBytes(), Metadata.of(multipartFile));
    }
}
