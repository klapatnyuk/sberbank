package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.Document;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.entity.Template;
import ru.klapatnyuk.sberbank.model.type.Boolean;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

        String sql = "SELECT d.id, d.title, d.edited, d.template_id, t.title " +
                "FROM document d, template t " +
                "WHERE d.active = TRUE AND d.template_id = t.id " +
                "ORDER BY d.id DESC";

        List<Document> result = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                Document entity;
                Template template;
                while (resultSet.next()) {
                    entity = new Document();
                    entity.setId(resultSet.getInt(1));
                    entity.setTitle(resultSet.getString(2));
                    entity.setEdited(resultSet.getTimestamp(3).toLocalDateTime());

                    template = new Template();
                    template.setId(resultSet.getInt(4));
                    template.setTitle(resultSet.getString(5));
                    entity.setTemplate(template);
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
}
