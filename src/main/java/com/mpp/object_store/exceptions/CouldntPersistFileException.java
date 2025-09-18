package com.mpp.object_store.exceptions;

public class CouldntPersistFileException extends RuntimeException {
    public CouldntPersistFileException(String message) {
        super(message);
    }
}
