package me.choicore.demo.filestorage.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileManager {

    private final FileProperties fileProperties;

    public String upload(final MultipartFile file) throws IOException {
        String generateUUID = UUID.randomUUID().toString();
        Path destination = Paths.get(fileProperties.baseDir(), generateUUID);
        file.transferTo(destination);
        return generateUUID;
    }
}
