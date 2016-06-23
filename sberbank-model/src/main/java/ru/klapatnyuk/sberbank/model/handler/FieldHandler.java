package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.Field;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author klapatnyuk
 */
public abstract class FieldHandler extends AbstractHandler<Field> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldHandler.class);

    public void removeExcept(int entityId, List<Integer> ids) throws SQLException {
        LOGGER.debug("Entering removeExcept(" + entityId + ", " + ids + ")");
        if (ids.isEmpty()) {
            return;
        }

        StringBuilder sql = new StringBuilder("UPDATE " + getTable() + " " +
                "SET active = FALSE " +
                "WHERE active = TRUE AND " + getEntityColumn() + " = ? AND id NOT IN (");
        for (int a = 0; a < ids.size(); a++) {
            if (a > 0) {
                sql.append(", ");
            }
            sql.append("?");
        }
        sql.append(")");
        try (PreparedStatement statement = getConnection().prepareStatement(sql.toString())) {
            statement.setInt(1, entityId);
            for (int a = 0; a < ids.size(); a++) {
                statement.setInt(a + 2, ids.get(a));
            }
            statement.executeUpdate();
        }
    }

    /**
     * TODO implement if necessary
     */
    @Override
    public List<Field> findAll() throws SQLException {
        return null;
    }

    public abstract List<Field> findByEntityId(int id) throws SQLException;

    public abstract void create(int entityId, List<Field> fields) throws SQLException;

    public abstract void createOrUpdate(int entityId, List<Field> fields) throws SQLException;

    protected abstract String getEntityColumn();
}
