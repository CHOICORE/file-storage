package me.choicore.demo.filestorage.presentation;

import lombok.RequiredArgsConstructor;
import me.choicore.demo.filestorage.common.FileManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/v1/files")
@RequiredArgsConstructor
public class FileUploadApi {

    private final FileManager fileManager;

    @PutMapping
    public ResponseEntity<?> upload(
            @RequestParam(name = "file") MultipartFile file
    ) throws IOException {

        String generateUUID = fileManager.upload(file);

        Map<String, Object> result = Map.of(
                "sid", generateUUID,
                "filename", Objects.requireNonNull(file.getOriginalFilename()),
                "size", file.getSize()
        );

        return ResponseEntity.ok(result);
    }
}
