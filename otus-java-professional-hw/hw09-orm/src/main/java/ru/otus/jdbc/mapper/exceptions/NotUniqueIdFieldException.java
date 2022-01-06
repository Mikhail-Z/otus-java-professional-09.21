package ru.otus.jdbc.mapper.exceptions;

public class NotUniqueIdFieldException extends RuntimeException {

    public NotUniqueIdFieldException() {}

    public NotUniqueIdFieldException(Exception e) {
        super(e);
    }
}
