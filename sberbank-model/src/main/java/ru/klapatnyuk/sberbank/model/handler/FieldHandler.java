package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.Field;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author klapatnyuk
 */
public class FieldHandler extends AbstractHandler<Field> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldHandler.class);

    public FieldHandler(Connection connection) {
        super(connection);
    }

    public List<Field> findByTemplateId(int id) throws SQLException {
        LOGGER.debug("Inside FieldHandler.findByTemplateId(" + id + ")");

        String sql = "SELECT id, title, label, type, \"order\" " +
                "FROM template_field " +
                "WHERE active = TRUE AND template_id = ? " +
                "ORDER BY \"order\"";

        List<Field> result = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                Field entity;
                while (resultSet.next()) {
                    entity = new Field();
                    entity.setId(resultSet.getInt(1));
                    entity.setTitle(resultSet.getString(2));
                    entity.setLabel(resultSet.getString(3));
                    entity.setType(Field.Type.find(resultSet.getString(4)));
                    entity.setOrder(resultSet.getInt(5));
                    result.add(entity);
                }
            }
        }
        return result;
    }
}
