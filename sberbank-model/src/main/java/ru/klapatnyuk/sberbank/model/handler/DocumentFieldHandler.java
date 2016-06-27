package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.handler.api.RemovableEntityHandler;
import ru.klapatnyuk.sberbank.model.type.Boolean;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author klapatnyuk
 */
public class DocumentFieldHandler extends FieldHandler implements RemovableEntityHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentFieldHandler.class);
    private static final String TABLE = "document_field";
    private static final String ENTITY_COLUMN = "document_id";

    @Override
    public List<Field> findByEntityId(int id) throws SQLException {
        LOGGER.debug("Entering findByEntityId(" + id + ")");

        String sql = "SELECT d.id, t.label, t.type, t.active, t.id, d.value " +
                "FROM (" +
                "   SELECT * " +
                "   FROM document_field " +
                "   WHERE active = TRUE" +
                ") d " +
                "RIGHT OUTER JOIN (" +
                "   SELECT tf.id AS id, tf.label AS label, tf.type AS type, tf.active AS active, tf.index AS index " +
                "   FROM template_field tf " +
                "       JOIN template t ON tf.template_id = t.id " +
                "       JOIN document d ON d.template_id = t.id " +
                "   WHERE d.id = ?" +
                ") t " +
                "ON d.template_field_id = t.id AND d.document_id = ? " +
                "ORDER BY t.active DESC, t.index";

        List<Field> result = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setInt(2, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Field.Type type = Field.Type.find(resultSet.getString(3));
                    switch (type) {
                        case LINE:
                        case AREA: {
                            result.add(Field.<String>newBuilder()
                                    .setId(resultSet.getInt(1))
                                    .setLabel(resultSet.getString(2))
                                    .setType(type)
                                    .setActive(resultSet.getBoolean(4))
                                    .setReferenceId(resultSet.getInt(5))
                                    .setValue(resultSet.getString(6))
                                    .build());
                            break;
                        }
                        case CHECKBOX: {
                            result.add(Field.<java.lang.Boolean>newBuilder()
                                    .setId(resultSet.getInt(1))
                                    .setLabel(resultSet.getString(2))
                                    .setType(type)
                                    .setActive(resultSet.getBoolean(4))
                                    .setReferenceId(resultSet.getInt(5))
                                    .setValue(Boolean.find(resultSet.getString(6)))
                                    .build());
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void create(int entityId, List<Field> fields) throws SQLException {
        LOGGER.debug("Entering create(" + entityId + ", List<Field>)");
        if (fields.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO document_field (document_id, template_field_id, value) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            Serializable value;
            for (Field field : fields) {
                statement.setInt(1, entityId);
                statement.setInt(2, field.getReferenceId());
                value = field.getValue();
                if (value instanceof String) {
                    statement.setString(3, (String) value);
                } else if (value instanceof java.lang.Boolean) {
                    statement.setString(3, Boolean.find((boolean) value));
                }
                statement.executeUpdate();
            }
        }
    }

    @Override
    public void createOrUpdate(int entityId, List<Field> fields) throws SQLException {
        LOGGER.debug("Entering insertOrUpdate(" + entityId + ", List<Field>)");

        String insertSql = "INSERT INTO document_field (document_id, template_field_id, value) " +
                "VALUES (?, ?, ?)";
        String updateSql = "UPDATE document_field " +
                "SET value = ? " +
                "WHERE active = TRUE AND id = ?";
        try (PreparedStatement insertStatement = getConnection().prepareStatement(insertSql);
             PreparedStatement updateStatement = getConnection().prepareStatement(updateSql)) {
            for (Field field : fields) {
                if (field.getId() == 0) {
                    // add new fields
                    insertStatement.setInt(1, entityId);
                    insertStatement.setInt(2, field.getReferenceId());
                    if (field.getValue() instanceof String) {
                        insertStatement.setString(3, field.getValue().toString());
                    } else if (field.getValue() instanceof java.lang.Boolean) {
                        insertStatement.setString(3, Boolean.find((boolean) field.getValue()));
                    }
                    insertStatement.executeUpdate();

                } else {
                    // edit existed fields
                    if (field.getValue() instanceof String) {
                        updateStatement.setString(1, field.getValue().toString());
                    } else if (field.getValue() instanceof java.lang.Boolean) {
                        updateStatement.setString(1, Boolean.find((boolean) field.getValue()));
                    }
                    updateStatement.setInt(2, field.getId());
                    updateStatement.executeUpdate();
                }
            }
        }
    }

    @Override
    public void remove(int documentId) throws SQLException {
        LOGGER.debug("Entering remove(" + documentId + ")");

        String sql = "UPDATE document_field " +
                "SET active = FALSE " +
                "WHERE active = TRUE AND document_id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, documentId);
            statement.executeUpdate();
        }
    }

    @Override
    protected String getTable() {
        return TABLE;
    }

    @Override
    protected String getEntityColumn() {
        return ENTITY_COLUMN;
    }
}
