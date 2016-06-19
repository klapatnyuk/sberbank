package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.*;
import ru.klapatnyuk.sberbank.model.type.Boolean;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author klapatnyuk
 */
public class DocumentHandler extends AbstractHandler<Document> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentHandler.class);

    public DocumentHandler(Connection connection) {
        super(connection);
    }

    @Override
    public List<Document> findAll() throws SQLException {
        LOGGER.debug("Inside DocumentHandler.findAll");

        String sql = "SELECT d.id, d.title, d.edited, d.template_id, t.title, d.owner_id " +
                "FROM document d, template t " +
                "WHERE d.active = TRUE AND d.template_id = t.id " +
                "ORDER BY d.id DESC";

        List<Document> result = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                Document entity;
                Template template;
                User user;
                while (resultSet.next()) {
                    entity = new Document();
                    entity.setId(resultSet.getInt(1));
                    entity.setTitle(resultSet.getString(2));
                    entity.setEdited(resultSet.getTimestamp(3).toLocalDateTime());

                    template = new Template();
                    template.setId(resultSet.getInt(4));
                    template.setTitle(resultSet.getString(5));
                    entity.setTemplate(template);

                    user = new User();
                    user.setId(resultSet.getInt(6));
                    entity.setOwner(user);

                    result.add(entity);
                }
            }
        }
        return result;
    }

    public int createDocument(Document document) throws SQLException {
        LOGGER.debug("Inside DocumentHandler.createDocument");

        String documentSql = "INSERT INTO document (template_id, title, owner_id) " +
                "VALUES (?, ?, ?)";

        int documentId;
        try (PreparedStatement statement = getConnection().prepareStatement(documentSql,
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, document.getTemplate().getId());
            statement.setString(2, document.getTitle());
            // TODO implement dynamic 'owner_id'
            statement.setInt(3, 1);
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    documentId = resultSet.getInt(1);
                } else {
                    throw new SQLException("Creating record failed, no id obtained");
                }
            }
        }

        if (document.getFields() == null || document.getFields().isEmpty()) {
            return documentId;
        }

        String fieldSql = "INSERT INTO document_field (document_id, template_field_id, value) " +
                "VALUES (?, ?, ?)";
        try (PreparedStatement statement = getConnection().prepareStatement(fieldSql)) {
            Serializable value;
            for (Field field : document.getFields()) {
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

        return documentId;
    }

    public void updateDocument(Document document) throws SQLException {
        LOGGER.debug("Inside DocumentHandler.updateDocument");

        // check concurrency updating

        String checkEditedSql = "SELECT edited " +
                "FROM document " +
                "WHERE active = TRUE AND id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(checkEditedSql)) {
            statement.setInt(1, document.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Checking concurrency updating failed (record not exists)");
                }
                if (resultSet.getTimestamp(1).toLocalDateTime().compareTo(document.getEdited()) > 0) {
                    throw new SQLException("Concurrency updating detected");
                }
            }
        }

        // update template

        String templateSql = "UPDATE document " +
                "SET title = ?, edited = ? " +
                "WHERE active = TRUE AND id = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(templateSql)) {
            statement.setString(1, document.getTitle());
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(3, document.getId());
            if (statement.executeUpdate() == 0) {
                throw new SQLException("Editing 'document' record failed, no affected records");
            }
        }

        // remove fields

        List<Integer> ids = document.getFields().stream().map(AbstractEntity::getId).collect(Collectors.toList());
        if (ids.isEmpty()) {
            String deleteSql = "UPDATE document_field " +
                    "SET active = FALSE " +
                    "WHERE active = TRUE AND document_id = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(deleteSql)) {
                statement.setInt(1, document.getId());
                statement.executeUpdate();
            }
        } else {
            StringBuilder deleteSql = new StringBuilder("UPDATE document_field " +
                    "SET active = FALSE " +
                    "WHERE active = TRUE AND document_id = ? AND id NOT IN (");
            for (int a = 0; a < ids.size(); a++) {
                if (a > 0) {
                    deleteSql.append(", ");
                }
                deleteSql.append("?");
            }
            deleteSql.append(")");
            try (PreparedStatement statement = getConnection().prepareStatement(deleteSql.toString())) {
                statement.setInt(1, document.getId());
                for (int a = 0; a < ids.size(); a++) {
                    statement.setInt(a + 2, ids.get(a));
                }
                statement.executeUpdate();
            }
        }

        // update fields

        String insertFieldSql = "INSERT INTO document_field (document_id, template_field_id, value) " +
                "VALUES (?, ?, ?)";
        String updateFieldSql = "UPDATE document_field " +
                "SET value = ? " +
                "WHERE active = TRUE AND id = ?";
        try (PreparedStatement insertStatement = getConnection().prepareStatement(insertFieldSql);
             PreparedStatement updateStatement = getConnection().prepareStatement(updateFieldSql)) {
            for (Field field : document.getFields()) {
                if (field.getId() == 0) {
                    // add new fields
                    insertStatement.setInt(1, document.getId());
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
}
