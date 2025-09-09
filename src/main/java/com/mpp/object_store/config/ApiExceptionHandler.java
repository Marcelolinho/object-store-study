package com.mpp.object_store.config;

import com.mpp.object_store.exceptions.CouldntPersistFile;
import com.mpp.object_store.exceptions.ServerSideException;
import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ApiError handleRuntimeException(RuntimeException ex) {
        return new ApiError(ex.getMessage(), HttpStatus.BAD_GATEWAY.value());
    }

    @ExceptionHandler(CouldntPersistFile.class)
    public ApiError handleCouldntPersistFile(CouldntPersistFile ex) {
        return new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ApiError handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(ServerSideException.class)
    public ApiError handleServerException(@NotNull ServerSideException ex) {
        return new ApiError(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
