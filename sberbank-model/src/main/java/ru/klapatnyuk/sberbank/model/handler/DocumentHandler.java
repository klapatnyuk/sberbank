package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.Document;
import ru.klapatnyuk.sberbank.model.entity.Template;
import ru.klapatnyuk.sberbank.model.entity.User;

import java.sql.*;
import java.time.LocalDateTime;
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

    public List<Document> findAll() throws SQLException {
        LOGGER.debug("Inside DocumentHandler.findAll");

        String sql = "SELECT d.id, d.title, d.edited, d.template_id, t.title, t.active, d.owner_id " +
                "FROM document d, template t " +
                "WHERE d.active = TRUE AND d.template_id = t.id " +
                "ORDER BY t.active DESC, d.id DESC";

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
                    template.setActive(resultSet.getBoolean(6));
                    entity.setTemplate(template);

                    user = new User();
                    user.setId(resultSet.getInt(7));
                    entity.setOwner(user);

                    result.add(entity);
                }
            }
        }
        return result;
    }

    public List<Document> findByOwnerId(int ownerId) throws SQLException {
        LOGGER.debug("Inside DocumentHandler.findByOwnerId(" + ownerId + ")");

        String sql = "SELECT d.id, d.title, d.edited, d.template_id, t.title, t.active " +
                "FROM document d, template t " +
                "WHERE d.active = TRUE AND d.template_id = t.id AND d.owner_id = ? " +
                "ORDER BY t.active DESC, d.id DESC";

        List<Document> result = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, ownerId);

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
                    template.setActive(resultSet.getBoolean(6));
                    entity.setTemplate(template);

                    user = new User();
                    user.setId(ownerId);
                    entity.setOwner(user);

                    result.add(entity);
                }
            }
        }
        return result;
    }

    public int createDocument(Document document) throws SQLException {
        LOGGER.debug("Inside DocumentHandler.createDocument");

        String sql = "INSERT INTO document (template_id, title, owner_id) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement statement = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, document.getTemplate().getId());
            statement.setString(2, document.getTitle());
            statement.setInt(3, document.getOwner().getId());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                } else {
                    throw new SQLException("Creating record failed, no id obtained");
                }
            }
        }
    }

    public void updateDocument(Document document) throws SQLException {
        LOGGER.debug("Inside DocumentHandler.updateDocument");

        String sql = "UPDATE document " +
                "SET title = ?, edited = ? " +
                "WHERE active = TRUE AND id = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, document.getTitle());
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(3, document.getId());
            if (statement.executeUpdate() == 0) {
                throw new SQLException("Editing 'document' record failed, no affected records");
            }
        }
    }

    public void removeDocument(int id) throws SQLException {
        LOGGER.debug("Inside DocumentHandler.removeDocument(" + id + ")");

        String sql = "UPDATE document " +
                "SET active = FALSE " +
                "WHERE active = TRUE AND id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public int compareEdited(int id, LocalDateTime edited) throws SQLException {
        LOGGER.debug("Inside DocumentHandler.compareEdited(" + id + ", " + edited + ")");

        String sql = "SELECT edited " +
                "FROM document " +
                "WHERE active = TRUE AND id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getTimestamp(1).toLocalDateTime().compareTo(edited);
                }
                throw new SQLException("Checking concurrency updating failed (record not exists)");
            }
        }
    }
}
