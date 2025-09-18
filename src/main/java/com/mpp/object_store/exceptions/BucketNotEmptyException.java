package com.mpp.object_store.exceptions;

public class BucketNotEmptyException extends RuntimeException {
    public BucketNotEmptyException(String message) {
        super(message);
    }
}
