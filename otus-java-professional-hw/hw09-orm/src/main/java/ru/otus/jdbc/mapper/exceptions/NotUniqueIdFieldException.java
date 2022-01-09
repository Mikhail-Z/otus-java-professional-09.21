package ru.otus.jdbc.mapper.exceptions;

public class NotUniqueIdFieldException extends OrmException {

    public NotUniqueIdFieldException() {
        this(new IllegalArgumentException("found several fields annotated with @Id, but expected exactly one field"));
    }

    public NotUniqueIdFieldException(Exception e) {
        super(e);
    }
}
