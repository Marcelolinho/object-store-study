package com.mpp.object_store.exceptions;

public class CouldntDeleteFileException extends RuntimeException {
    public CouldntDeleteFileException(String message) {
        super(message);
    }
}
