package org.example.exception;

public class QueryException extends RuntimeException{

    /**
     * Constructs a new {@code QueryException} with the specified detail message.
     * The detail message is saved for later retrieval by the {@link #getMessage()} method.
     *
     * @param exception is the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public QueryException(String exception) {
        super(exception);
    }
}
