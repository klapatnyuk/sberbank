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

    public int create(Template template) throws SQLException {
        LOGGER.debug("Entering create");

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

    @Override
    public List<Template> findAll() throws SQLException {
        LOGGER.debug("Entering findAll");

        String sql = "SELECT id, title, edited " +
                "FROM template " +
                "WHERE active = TRUE " +
                "ORDER BY id DESC";

        List<Template> result = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(Template.newBuilder()
                            .setId(resultSet.getInt(1))
                            .setTitle(resultSet.getString(2))
                            .setEdited(resultSet.getTimestamp(3).toLocalDateTime())
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
