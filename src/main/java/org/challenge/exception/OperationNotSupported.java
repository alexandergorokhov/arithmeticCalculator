package org.challenge.exception;

public class OperationNotSupported extends RuntimeException{
    public OperationNotSupported(String message) {
        super(message);
    }
}
