package ru.otus.jdbc.mapper.utils;

import ru.otus.jdbc.mapper.exceptions.ResultSetMappingException;
import ru.otus.jdbc.mapper.metadata.EntityClassMetaData;

import java.sql.ResultSet;

public class ResultSetMapperImpl<T> implements ResultSetMapper<T> {

    private final EntityClassMetaData<T> entityClassMetaData;

    public ResultSetMapperImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public T map(ResultSet resultSet) {
        var columns = entityClassMetaData.getAllFields();
        try {
            var klass = columns.get(0).getDeclaringClass();
            var constructor  = klass.getConstructor();
            constructor.setAccessible(true);

            var instance = (T)constructor.newInstance();
            for (var column: columns) {
                var columnValue = resultSet.getObject(column.getName());
                var field = klass.getDeclaredField(column.getName());
                field.setAccessible(true);
                field.set(instance, columnValue);
            }

            return instance;
        }
        catch (Exception e) {
            throw new ResultSetMappingException(e);
        }
    }
}
