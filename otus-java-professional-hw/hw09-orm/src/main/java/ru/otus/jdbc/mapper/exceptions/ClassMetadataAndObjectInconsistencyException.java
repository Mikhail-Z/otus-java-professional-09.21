package ru.otus.jdbc.mapper.exceptions;

public class ClassMetadataAndObjectInconsistencyException extends RuntimeException {
    public ClassMetadataAndObjectInconsistencyException(Exception e) {
        super(e);
    }
}
