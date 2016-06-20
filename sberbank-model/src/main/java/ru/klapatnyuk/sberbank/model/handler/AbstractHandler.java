package ru.klapatnyuk.sberbank.model.handler;

import ru.klapatnyuk.sberbank.model.entity.api.Entity;

import java.sql.Connection;

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

    protected Connection getConnection() {
        return connection;
    }
}
