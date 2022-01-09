package ru.otus.jdbc.mapper.exceptions;

public class NotFoundIdFieldException extends OrmException {
    public NotFoundIdFieldException() {
        this(new IllegalArgumentException("not found field annotated with @Id"));
    }

    public NotFoundIdFieldException(Exception e) {
        super(e);
    }
}
