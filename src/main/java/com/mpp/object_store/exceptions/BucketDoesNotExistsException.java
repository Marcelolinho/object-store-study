package com.mpp.object_store.exceptions;

public class BucketDoesNotExistsException extends RuntimeException {
    public BucketDoesNotExistsException(String message) {
        super(message);
    }
}
