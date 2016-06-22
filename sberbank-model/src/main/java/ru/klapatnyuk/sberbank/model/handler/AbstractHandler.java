package ru.klapatnyuk.sberbank.model.handler;

import java.sql.Connection;

/**
 * @author klapatnyuk
 */
public abstract class AbstractHandler {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    protected abstract String getTable();
}
