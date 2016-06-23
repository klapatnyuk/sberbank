package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.Field;
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
public class DocumentFieldHandler extends FieldHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentFieldHandler.class);
    private static final String TABLE = "document_field";

    public List<Field> findByDocumentId(int id) throws SQLException {
        LOGGER.debug("Entering findByDocumentId(" + id + ")");

        String sql = "SELECT d.id, t.label, t.type, t.active, t.id, d.value " +
                "FROM (" +
                "   SELECT * " +
                "   FROM document_field " +
                "   WHERE active = TRUE" +
                ") d " +
                "RIGHT OUTER JOIN (" +
                "   SELECT tf.id AS id, tf.label AS label, tf.type AS type, tf.active AS active, tf.\"order\" AS \"order\" " +
                "   FROM template_field tf " +
                "       JOIN template t ON tf.template_id = t.id " +
                "       JOIN document d ON d.template_id = t.id " +
                "   WHERE d.id = ?" +
                ") t " +
                "ON d.template_field_id = t.id AND d.document_id = ? " +
                "ORDER BY t.active DESC, t.\"order\"";

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
                            Field<String> entity = new Field<>();
                            entity.setId(resultSet.getInt(1));
                            entity.setLabel(resultSet.getString(2));
                            entity.setType(type);
                            entity.setActive(resultSet.getBoolean(4));
                            entity.setReferenceId(resultSet.getInt(5));
                            entity.setValue(resultSet.getString(6));

                            result.add(entity);
                            break;
                        }
                        case CHECKBOX: {
                            Field<java.lang.Boolean> entity = new Field<>();
                            entity.setId(resultSet.getInt(1));
                            entity.setLabel(resultSet.getString(2));
                            entity.setType(type);
                            entity.setActive(resultSet.getBoolean(4));
                            entity.setReferenceId(resultSet.getInt(5));
                            entity.setValue(Boolean.find(resultSet.getString(6)));

                            result.add(entity);
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    public void createDocumentFields(int documentId, List<Field> fields) throws SQLException {
        LOGGER.debug("Entering createDocumentFields(" + documentId + ", List<Field>)");
        if (fields.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO document_field (document_id, template_field_id, value) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            Serializable value;
            for (Field field : fields) {
                statement.setInt(1, documentId);
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

    public void removeDocumentFieldsExcept(int documentId, List<Integer> ids) throws SQLException {
        LOGGER.debug("Entering removeDocumentFieldsExcept(" + documentId + ", " + ids + ")");
        if (ids.isEmpty()) {
            return;
        }

        StringBuilder sql = new StringBuilder("UPDATE document_field " +
                "SET active = FALSE " +
                "WHERE active = TRUE AND document_id = ? AND id NOT IN (");
        for (int a = 0; a < ids.size(); a++) {
            if (a > 0) {
                sql.append(", ");
            }
            sql.append("?");
        }
        sql.append(")");
        try (PreparedStatement statement = getConnection().prepareStatement(sql.toString())) {
            statement.setInt(1, documentId);
            for (int a = 0; a < ids.size(); a++) {
                statement.setInt(a + 2, ids.get(a));
            }
            statement.executeUpdate();
        }
    }

    public void removeDocumentFields(int documentId) throws SQLException {
        LOGGER.debug("Entering removeDocumentFields(" + documentId + ")");

        String sql = "UPDATE document_field " +
                "SET active = FALSE " +
                "WHERE active = TRUE AND document_id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, documentId);
            statement.executeUpdate();
        }
    }

    public void insertAndUpdateDocumentFields(int documentId, List<Field> fields) throws SQLException {
        LOGGER.debug("Entering insertAndUpdateDocumentFields(" + documentId + ", List<Field>)");

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
                    insertStatement.setInt(1, documentId);
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
    protected String getTable() {
        return TABLE;
    }
}
