package ru.otus.jdbc.mapper.utils;

import java.util.Map;

public interface FieldValuesExtractor<T> {
    Map<String, Object> extractAll(T instance);
    Map<String, Object> extractWithoutId(T instance);
}
