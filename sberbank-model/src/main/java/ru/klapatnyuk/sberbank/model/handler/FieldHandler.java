package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.type.Boolean;

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

    public List<Field> findByDocumentId(int id) throws SQLException {
        LOGGER.debug("Inside FieldHandler.findByDocumentId(" + id + ")");

        String sql = "SELECT d.id, t.label, t.type, t.active, d.value " +
                "FROM document_field d, template_field t " +
                "WHERE d.active = TRUE AND d.template_field_id = t.id AND d.document_id = ? " +
                "ORDER BY t.order";

        List<Field> result = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Field.Type type = Field.Type.find(resultSet.getString(3));
                    switch (type) {
                        case LINE:
                        case AREA: {
                            Field<String> entity = new Field<>();
                            entity.setId(resultSet.getInt(1));
                            entity.setTitle(resultSet.getString(2));
                            entity.setType(type);
                            entity.setActive(resultSet.getBoolean(4));
                            entity.setValue(resultSet.getString(5));

                            result.add(entity);
                            break;
                        }
                        case CHECKBOX: {
                            Field<java.lang.Boolean> entity = new Field<>();
                            entity.setId(resultSet.getInt(1));
                            entity.setTitle(resultSet.getString(2));
                            entity.setType(type);
                            entity.setActive(resultSet.getBoolean(4));
                            entity.setValue(Boolean.find(resultSet.getString(5)));

                            result.add(entity);
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<Field> findAll() throws SQLException {
        // TODO implement
        return null;
    }
}
