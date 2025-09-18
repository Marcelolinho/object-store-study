package com.mpp.object_store.exceptions;

public class FileAlreadyExistsBucketException extends RuntimeException  {
    public FileAlreadyExistsBucketException(String message) {
        super(message);
    }
}
