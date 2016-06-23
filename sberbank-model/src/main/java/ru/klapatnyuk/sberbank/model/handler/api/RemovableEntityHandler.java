package ru.klapatnyuk.sberbank.model.handler.api;

import java.sql.SQLException;

/**
 * @author klapatnyuk
 */
public interface RemovableEntityHandler {

    void remove(int id) throws SQLException;
}
