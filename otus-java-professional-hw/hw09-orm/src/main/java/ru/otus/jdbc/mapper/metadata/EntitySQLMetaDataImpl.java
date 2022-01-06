package ru.otus.jdbc.mapper.metadata;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        var tableName = getTableName();
        var tableColumns = getAllTableColumns();

        StringBuilder stringBuilder = new StringBuilder();
        addSelectPartSql(stringBuilder, tableName, tableColumns);
        stringBuilder.append(";");

        return stringBuilder.toString();
    }

    @Override
    public String getSelectByIdSql() {
        var tableName = getTableName();
        var tableColumns = getAllTableColumns();
        var idTableColumn = getIdColumnName();

        var stringBuilder = new StringBuilder();
        addSelectPartSql(stringBuilder, tableName, tableColumns);
        addWherePartSql(stringBuilder, List.of(idTableColumn));
        stringBuilder.append(";");

        return stringBuilder.toString();
    }

    @Override
    public String getInsertSql() {
        var tableName = getTableName();
        var tableColumns = getNotIdTableColumns();

        var stringBuilder = new StringBuilder();
        addInsertPartSql(stringBuilder, tableName, tableColumns);
        stringBuilder.append(";");

        return stringBuilder.toString();
    }

    @Override
    public String getUpdateSql() {
        var tableName = getTableName();
        var tableColumns = getAllTableColumns();
        var notIdColumns = getNotIdTableColumns();

        var stringBuilder = new StringBuilder();
        addUpdatePartSql(stringBuilder, tableName, tableColumns);
        addWherePartSql(stringBuilder, notIdColumns);
        stringBuilder.append(";");

        return stringBuilder.toString();
    }

    private String getTableName() {
        return entityClassMetaData.getName();
    }

    private List<String> getAllTableColumns() {
        return entityClassMetaData.getAllFields()
                .stream()
                .map(Field::getName)
                .toList();
    }

    private List<String> getNotIdTableColumns() {
        return entityClassMetaData.getFieldsWithoutId()
                .stream()
                .map(Field::getName)
                .toList();
    }

    private String getIdColumnName() {
        return entityClassMetaData.getIdField().getName();
    }

    private void addSelectPartSql(StringBuilder stringBuilder, String tableName, List<String> tableColumns) {
        stringBuilder.append("SELECT ");
        stringBuilder.append(tableColumns.get(0));
        for (int i = 1; i < tableColumns.size(); i++) {
            stringBuilder.append(", ")
                    .append(tableColumns.get(i));
        }
        stringBuilder.append(" FROM ")
                .append(tableName)
                .append("\n");
    }

    private void addWherePartSql(StringBuilder stringBuilder, List<String> whereColumns) {
        stringBuilder.append("where ");
        stringBuilder.append(whereColumns.get(0)).append(" = ?");
        for (int i = 1; i < whereColumns.size(); i++) {
            stringBuilder.append(whereColumns.get(i)).append(" = ?");
        }
        stringBuilder.append("\n");
    }

    private void addInsertPartSql(StringBuilder stringBuilder, String tableName, List<String> insertedColumns) {
        stringBuilder.append("INSERT INTO ").append(tableName).append("(");
        stringBuilder.append(insertedColumns.get(0));
        for (int i = 1; i < insertedColumns.size(); i++) {
            stringBuilder.append(", ").append(insertedColumns.get(i));
        }

        stringBuilder.append(") VALUES(");
        stringBuilder.append("?");
        stringBuilder.append(", ?".repeat(insertedColumns.size() - 1));
        stringBuilder.append(")");
        stringBuilder.append("\n");
    }

    private void addUpdatePartSql(StringBuilder stringBuilder, String tableName, List<String> updatedColumns) {
        stringBuilder.append("UPDATE ").append(tableName).append(" SET ");
        stringBuilder.append(updatedColumns.get(0)).append(" = ?");
        for (int i = 1; i < updatedColumns.size(); i++) {
            stringBuilder.append(", ").append(updatedColumns.get(i)).append(" = ?");
        }
        stringBuilder.append("\n");
    }
}
