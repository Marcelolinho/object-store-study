package com.mpp.object_store.exceptions;

public class BucketAlreadyEmpty extends RuntimeException {
    public BucketAlreadyEmpty(String message) {
        super(message);
    }
}
