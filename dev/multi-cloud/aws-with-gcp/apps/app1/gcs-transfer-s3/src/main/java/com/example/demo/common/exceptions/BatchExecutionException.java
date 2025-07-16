package com.example.demo.common.exceptions;

public class BatchExecutionException extends Exception {

    public BatchExecutionException(String message) {
        super(message);
    }

    public BatchExecutionException(String message, Exception origin) {
        super(message, origin);
    }
}
