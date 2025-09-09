package com.mpp.object_store.exceptions;

public class BucketNotEmpty extends RuntimeException {
    public BucketNotEmpty(String message) {
        super(message);
    }
}
