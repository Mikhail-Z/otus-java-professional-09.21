package ru.otus.jdbc.mapper.exceptions;

public class NotFoundIdFieldException extends RuntimeException {
    public NotFoundIdFieldException() {}

    public NotFoundIdFieldException(Exception e) {
        super(e);
    }
}
