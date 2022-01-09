package ru.otus.jdbc.mapper.utils;

import java.sql.ResultSet;

public interface ResultSetMapper<T> {
    T map(ResultSet resultSet);
}
