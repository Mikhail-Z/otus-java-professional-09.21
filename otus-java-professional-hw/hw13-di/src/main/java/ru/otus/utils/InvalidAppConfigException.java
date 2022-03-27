package ru.otus.utils;

public class InvalidAppConfigException extends RuntimeException {
    public InvalidAppConfigException(String message) {
        super(String.format("these component names are not unique: %s", message));
    }
}
