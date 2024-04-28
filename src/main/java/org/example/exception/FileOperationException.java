package org.example.exception;

public class FileOperationException extends RuntimeException{

    /**
     * Constructs a new {@code FileOperationException} with the specified detail message.
     * The detail message is saved for later retrieval by the {@link #getMessage()} method.
     *
     * @param exception is the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public FileOperationException(String exception) {
        super(exception);
    }
}
