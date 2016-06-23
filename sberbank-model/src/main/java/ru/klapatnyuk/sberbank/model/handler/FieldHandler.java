package ru.klapatnyuk.sberbank.model.handler;

import ru.klapatnyuk.sberbank.model.entity.Field;

import java.sql.SQLException;
import java.util.List;

/**
 * TODO needs to be refactored
 *
 * @author klapatnyuk
 */
public abstract class FieldHandler extends AbstractHandler<Field> {

    /**
     * TODO implement if necessary
     */
    @Override
    public List<Field> findAll() throws SQLException {
        return null;
    }
}
