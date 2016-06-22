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
public class FieldHandler extends AbstractHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldHandler.class);

    public List<Field> findByTemplateId(int id) throws SQLException {
        LOGGER.debug("Entering FieldHandler.findByTemplateId(" + id + ")");

        String sql = "SELECT t.id, t.title, t.label, t.type, t.\"order\", c.count " +
                "FROM ( " +
                "   SELECT id, title, label, type, \"order\" " +
                "   FROM template_field " +
                "   WHERE active = TRUE AND template_id = ? " +
                ") t " +
                "LEFT OUTER JOIN ( " +
                "   SELECT t_f.id, COUNT(*) AS count " +
                "   FROM document_field d_f " +
                "       JOIN document d ON d_f.document_id = d.id AND d.active = TRUE " +
                "       JOIN template t ON d.template_id = t.id AND t.id = ? " +
                "       JOIN template_field t_f ON d_f.template_field_id = t_f.id AND t_f.active = TRUE " +
                "   WHERE d_f.active = TRUE " +
                "   GROUP BY t_f.id " +
                ") c " +
                "ON c.id = t.id " +
                "ORDER BY \"order\";";

        List<Field> result = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setInt(2, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                Field entity;
                while (resultSet.next()) {
                    entity = new Field();
                    entity.setId(resultSet.getInt(1));
                    entity.setTitle(resultSet.getString(2));
                    entity.setLabel(resultSet.getString(3));
                    entity.setType(Field.Type.find(resultSet.getString(4)));
                    entity.setOrder(resultSet.getInt(5));
                    entity.setRelated(resultSet.getInt(6));
                    result.add(entity);
                }
            }
        }
        return result;
    }

    public List<Field> findByDocumentId(int id) throws SQLException {
        LOGGER.debug("Entering FieldHandler.findByDocumentId(" + id + ")");

        String sql = "SELECT d.id, t.label, t.type, t.active, t.id, d.value " +
                "FROM (" +
                "   SELECT * " +
                "   FROM document_field " +
                "   WHERE active = TRUE" +
                ") d " +
                "RIGHT OUTER JOIN (" +
                "   SELECT tf.id as id, tf.label as label, tf.type as type, tf.active as active, tf.\"order\" as \"order\" " +
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
        LOGGER.debug("Entering FieldHandler.createDocumentFields(" + documentId + ", List<Field>)");
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

    public void createTemplateFields(int templateId, List<Field> fields) throws SQLException {
        LOGGER.debug("Entering FieldHandler.createTemplateFields(" + templateId + ", List<Field>)");
        if (fields.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO template_field (template_id, title, label, type, \"order\") " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            int order = 0;
            for (Field field : fields) {
                statement.setInt(1, templateId);
                statement.setString(2, field.getTitle());
                statement.setString(3, field.getLabel());
                statement.setString(4, field.getType().toString());
                statement.setInt(5, order++);
                statement.executeUpdate();
            }
        }
    }

    public void removeDocumentFieldsExcept(int documentId, List<Integer> ids) throws SQLException {
        LOGGER.debug("Entering FieldHandler.removeDocumentFieldsExcept(" + documentId + ", " + ids + ")");
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

    public void removeTemplateFieldsExcept(int templateId, List<Integer> ids) throws SQLException {
        LOGGER.debug("Entering FieldHandler.removeTemplateFieldsExcept(" + templateId + ", " + ids + ")");
        if (ids.isEmpty()) {
            return;
        }

        StringBuilder sql = new StringBuilder("UPDATE template_field " +
                "SET active = FALSE " +
                "WHERE active = TRUE AND template_id = ? AND id NOT IN (");
        for (int a = 0; a < ids.size(); a++) {
            if (a > 0) {
                sql.append(", ");
            }
            sql.append("?");
        }
        sql.append(")");
        try (PreparedStatement statement = getConnection().prepareStatement(sql.toString())) {
            statement.setInt(1, templateId);
            for (int a = 0; a < ids.size(); a++) {
                statement.setInt(a + 2, ids.get(a));
            }
            statement.executeUpdate();
        }
    }

    public void removeDocumentFields(int documentId) throws SQLException {
        LOGGER.debug("Entering FieldHandler.removeDocumentFields(" + documentId + ")");

        String sql = "UPDATE document_field " +
                "SET active = FALSE " +
                "WHERE active = TRUE AND document_id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, documentId);
            statement.executeUpdate();
        }
    }

    public void insertAndUpdateDocumentFields(int documentId, List<Field> fields) throws SQLException {
        LOGGER.debug("Entering FieldHandler.insertAndUpdateDocumentFields(" + documentId + ", List<Field>)");

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

    public void insertAndUpdateTemplateFields(int templateId, List<Field> fields) throws SQLException {
        LOGGER.debug("Entering FieldHandler.insertAndUpdateTemplateFields(" + templateId + ", List<Field>)");

        String insertSql = "INSERT INTO template_field (template_id, title, label, type, \"order\") " +
                "VALUES (?, ?, ?, ?, ?)";
        String updateSql = "UPDATE template_field " +
                "SET template_id = ?, title = ?, label = ?, type = ?, \"order\" = ? " +
                "WHERE active = TRUE AND id = ?";
        try (PreparedStatement insertStatement = getConnection().prepareStatement(insertSql);
             PreparedStatement updateStatement = getConnection().prepareStatement(updateSql)) {
            int order = 0;
            for (Field field : fields) {
                if (field.getId() == 0) {
                    // add new fields
                    insertStatement.setInt(1, templateId);
                    insertStatement.setString(2, field.getTitle());
                    insertStatement.setString(3, field.getLabel());
                    insertStatement.setString(4, field.getType().toString());
                    insertStatement.setInt(5, order++);
                    insertStatement.executeUpdate();
                } else {
                    // edit existed fields
                    updateStatement.setInt(1, templateId);
                    updateStatement.setString(2, field.getTitle());
                    updateStatement.setString(3, field.getLabel());
                    updateStatement.setString(4, field.getType().toString());
                    updateStatement.setInt(5, order++);
                    updateStatement.setInt(6, field.getId());
                    updateStatement.executeUpdate();
                }
            }
        }
    }
}
