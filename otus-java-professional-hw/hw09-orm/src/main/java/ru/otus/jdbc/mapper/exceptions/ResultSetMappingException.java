package ru.otus.jdbc.mapper.exceptions;

public class ResultSetMappingException extends OrmException {
    public ResultSetMappingException(Exception e) {
        super(e);
    }
}
