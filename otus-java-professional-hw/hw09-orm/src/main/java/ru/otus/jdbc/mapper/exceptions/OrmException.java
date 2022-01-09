package ru.otus.jdbc.mapper.exceptions;

public class OrmException extends RuntimeException {
    public OrmException(Exception e) {
        super(e);
    }
}
