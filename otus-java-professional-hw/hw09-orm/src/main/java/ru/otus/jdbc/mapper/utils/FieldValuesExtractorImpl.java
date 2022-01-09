package ru.otus.jdbc.mapper.utils;

import ru.otus.jdbc.mapper.exceptions.ClassMetadataAndObjectInconsistencyException;
import ru.otus.jdbc.mapper.metadata.EntityClassMetaData;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldValuesExtractorImpl<T> implements FieldValuesExtractor<T> {
    private final EntityClassMetaData<T> entityClassMetaData;

    public FieldValuesExtractorImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Map<String, Object> extractAll(T instance) {
        var fields = entityClassMetaData.getAllFields();
        return getFieldsAndValues(instance, fields);
    }

    @Override
    public Map<String, Object> extractWithoutId(T instance) {
        var fields = entityClassMetaData.getFieldsWithoutId();
        return getFieldsAndValues(instance, fields);
    }

    private Map<String, Object> getFieldsAndValues(T instance, List<Field> fields) {
        try {
            Map<String, Object> fieldsAndValues = new HashMap<>();
            for (var field: fields) {
                field.setAccessible(true);
                var value = field.get(instance);
                fieldsAndValues.put(field.getName(), value);
            }
            return fieldsAndValues;
        } catch (IllegalAccessException e) {
            throw new ClassMetadataAndObjectInconsistencyException(e);
        }
    }
}
