package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.Template;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author klapatnyuk
 */
public class TemplateHandler extends EditableEntityHandler<Template> {

    public static final String TABLE = "template";

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateHandler.class);

    public List<Template> findAll() throws SQLException {
        LOGGER.debug("Entering TemplateHandler.findAll");

        String sql = "SELECT id, title, edited " +
                "FROM template " +
                "WHERE active = TRUE " +
                "ORDER BY id DESC";

        List<Template> result = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                Template entity;
                while (resultSet.next()) {
                    entity = new Template();
                    entity.setId(resultSet.getInt(1));
                    entity.setTitle(resultSet.getString(2));
                    entity.setEdited(resultSet.getTimestamp(3).toLocalDateTime());
                    result.add(entity);
                }
            }
        }
        return result;
    }

    public int createTemplate(Template template) throws SQLException {
        LOGGER.debug("Entering TemplateHandler.createTemplate");

        String sql = "INSERT INTO template (title) " +
                "VALUES (?)";

        try (PreparedStatement statement = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, template.getTitle());
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

    public void removeTemplate(int id) throws SQLException {
        LOGGER.debug("Entering DocumentHandler.removeTemplate(" + id + ")");

        String sql = "UPDATE template " +
                "SET active = FALSE " +
                "WHERE active = TRUE AND id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    protected String getTable() {
        return TABLE;
    }
}
