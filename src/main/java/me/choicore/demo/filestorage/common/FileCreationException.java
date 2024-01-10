package me.choicore.demo.filestorage.common;

class FileCreationException extends RuntimeException {
    public FileCreationException(Throwable cause) {
        super("Creation of upload directory failed", cause);
    }
}
