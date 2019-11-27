package com.demo.configserver.exception;

/**
 * @author sandylian
 * @date 2019/11/26 22:22
 **/
public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
