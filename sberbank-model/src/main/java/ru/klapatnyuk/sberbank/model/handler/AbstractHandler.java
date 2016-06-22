package ru.klapatnyuk.sberbank.model.handler;

import ru.klapatnyuk.sberbank.model.entity.api.Entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author klapatnyuk
 */
public abstract class AbstractHandler<T extends Entity> {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public abstract List<T> findAll() throws SQLException;

    protected abstract String getTable();
}
