package me.choicore.demo.filestorage.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@ConfigurationProperties(prefix = "file")
public record FileProperties(
        String baseDir
) {
    public FileProperties(String baseDir) {
        this.baseDir = baseDir;
        validate();
    }

    private void validate() {
        if (!StringUtils.hasText(this.baseDir)) {
            throw new IllegalArgumentException("baseDir must not be empty");
        }

        createDirectoryIfNotExists();

        log.debug("FileProperties has been successfully validated: {}", this);
    }

    private void createDirectoryIfNotExists() {
        Path uploadDir = Path.of(this.baseDir);
        if (!Files.exists(uploadDir)) {
            try {
                Files.createDirectories(uploadDir);
                log.info("Upload directory has been successfully created: {}", uploadDir.toAbsolutePath());
            } catch (IOException e) {
                throw new FileCreationException(e);
            }
        }
    }
}

