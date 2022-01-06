package ru.otus.jdbc.mapper.exceptions;

public class ResultSetMappingException extends RuntimeException {
    public ResultSetMappingException(Exception e) {
        super(e);
    }
}
