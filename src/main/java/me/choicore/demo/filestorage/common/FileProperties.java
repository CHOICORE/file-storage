package me.choicore.demo.filestorage.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public record FileProperties(
        String baseDir
) {
}
