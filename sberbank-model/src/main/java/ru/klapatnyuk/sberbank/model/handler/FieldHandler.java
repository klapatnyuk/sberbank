package ru.klapatnyuk.sberbank.model.handler;

import ru.klapatnyuk.sberbank.model.entity.Field;

import java.sql.SQLException;
import java.util.List;

/**
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

    public abstract List<Field> findByEntityId(int id) throws SQLException;

    public abstract void create(int entityId, List<Field> fields) throws SQLException;

    public abstract void removeExcept(int entityId, List<Integer> ids) throws SQLException;

    public abstract void insertOrUpdate(int entityId, List<Field> fields) throws SQLException;
}
