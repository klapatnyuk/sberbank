package ru.klapatnyuk.sberbank.model.handler;

import ru.klapatnyuk.sberbank.model.entity.api.Entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * TODO handle generic type
 *
 * @author klapatnyuk
 */
public abstract class AbstractHandler<T extends Entity> {

    private final Connection connection;

    public AbstractHandler(Connection connection) {
        this.connection = connection;
    }

    public abstract List<T> findAll() throws SQLException;

    protected Connection getConnection() {
        return connection;
    }
}
