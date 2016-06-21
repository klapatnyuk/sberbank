package ru.klapatnyuk.sberbank.logic.api;

import java.sql.Connection;

/**
 * @author klapatnyuk
 */
public interface BusinessService {

    void setConnection(Connection connection);
}
