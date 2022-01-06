package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.jdbc.mapper.utils.FieldValuesExtractor;
import ru.otus.jdbc.mapper.utils.ResultSetMapper;
import ru.otus.jdbc.mapper.metadata.EntitySQLMetaData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static java.util.Collections.emptyList;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final ResultSetMapper<T> resultSetMapper;
    private final FieldValuesExtractor<T> fieldValuesExtractor;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, ResultSetMapper<T> resultSetMapper, FieldValuesExtractor<T> fieldValuesExtractor) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.resultSetMapper = resultSetMapper;
        this.fieldValuesExtractor = fieldValuesExtractor;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        var sql = entitySQLMetaData.getSelectByIdSql();
        return dbExecutor.executeSelect(connection, sql, List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return resultSetMapper.map(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        var sql = entitySQLMetaData.getSelectAllSql();
        return dbExecutor.executeSelect(connection, sql, emptyList(), rs -> {
            List<T> resultObjects = new LinkedList<>();
            try {
                while (rs.next()) {
                    resultObjects.add(resultSetMapper.map(rs));
                }
                return resultObjects;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        var sql = entitySQLMetaData.getInsertSql();
        var parameters = fieldValuesExtractor.extractWithoutId(client)
                .values()
                .stream()
                .toList();

        return dbExecutor.executeStatement(connection, sql, parameters);
    }

    @Override
    public void update(Connection connection, T client) {
        var sql = entitySQLMetaData.getUpdateSql();
        var parameters = fieldValuesExtractor.extractWithoutId(client)
                .values()
                .stream()
                .toList();
        dbExecutor.executeStatement(connection, sql, parameters);
    }

}
