package com.mpp.object_store.exceptions;

public class CouldntDeleteFile extends RuntimeException {
    public CouldntDeleteFile(String message) {
        super(message);
    }
}
