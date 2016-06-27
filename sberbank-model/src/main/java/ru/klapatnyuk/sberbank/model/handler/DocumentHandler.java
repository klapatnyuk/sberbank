package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.Document;
import ru.klapatnyuk.sberbank.model.entity.Template;
import ru.klapatnyuk.sberbank.model.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author klapatnyuk
 */
public class DocumentHandler extends EditableEntityHandler<Document> {

    public static final String TABLE = "document";

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentHandler.class);

    public List<Document> findByOwnerId(int ownerId) throws SQLException {
        LOGGER.debug("Entering findByOwnerId(" + ownerId + ")");

        String sql = "SELECT d.id, d.title, d.edited, d.template_id, t.title, t.active " +
                "FROM document d, template t " +
                "WHERE d.active = TRUE AND d.template_id = t.id AND d.owner_id = ? " +
                "ORDER BY t.active DESC, d.id DESC";

        List<Document> result = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, ownerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(Document.newBuilder()
                            .setId(resultSet.getInt(1))
                            .setTitle(resultSet.getString(2))
                            .setEdited(resultSet.getTimestamp(3).toLocalDateTime())
                            .setTemplate(Template.newBuilder()
                                    .setId(resultSet.getInt(4))
                                    .setTitle(resultSet.getString(5))
                                    .setActive(resultSet.getBoolean(6))
                                    .build())
                            .setOwner(User.newBuilder()
                                    .setId(ownerId)
                                    .build())
                            .build());
                }
            }
        }
        return result;
    }

    public int create(Document document) throws SQLException {
        LOGGER.debug("Entering create");

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

    @Override
    public List<Document> findAll() throws SQLException {
        LOGGER.debug("Entering findAll");

        String sql = "SELECT d.id, d.title, d.edited, d.template_id, t.title, t.active, d.owner_id " +
                "FROM document d, template t " +
                "WHERE d.active = TRUE AND d.template_id = t.id " +
                "ORDER BY t.active DESC, d.id DESC";

        List<Document> result = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(Document.newBuilder()
                            .setId(resultSet.getInt(1))
                            .setTitle(resultSet.getString(2))
                            .setEdited(resultSet.getTimestamp(3).toLocalDateTime())
                            .setTemplate(Template.newBuilder()
                                    .setId(resultSet.getInt(4))
                                    .setTitle(resultSet.getString(5))
                                    .setActive(resultSet.getBoolean(6))
                                    .build())
                            .setOwner(User.newBuilder()
                                    .setId(resultSet.getInt(7))
                                    .build())
                            .build());
                }
            }
        }
        return result;
    }

    @Override
    protected String getTable() {
        return TABLE;
    }
}
