package ru.otus.jdbc.mapper.exceptions;

public class ClassMetadataAndObjectInconsistencyException extends OrmException {
    public ClassMetadataAndObjectInconsistencyException(Exception e) {
        super(e);
    }
}
