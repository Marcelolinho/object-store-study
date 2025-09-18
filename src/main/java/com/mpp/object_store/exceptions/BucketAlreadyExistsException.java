package com.mpp.object_store.exceptions;

public class BucketAlreadyExistsException extends RuntimeException {
    public BucketAlreadyExistsException(String message) {
        super(message);
    }
}
