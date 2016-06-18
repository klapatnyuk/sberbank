package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.entity.Template;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author klapatnyuk
 */
public class TemplateHandler extends AbstractHandler<Template> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateHandler.class);

    public TemplateHandler(Connection connection) {
        super(connection);
    }

    public List<Template> findAll() throws SQLException {
        LOGGER.debug("Inside TemplateHandler.findAll");

        String sql = "SELECT id, title " +
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
                    result.add(entity);
                }
            }
        }
        return result;
    }

    public int createTemplate(Template template) throws SQLException {
        LOGGER.debug("Inside TemplateHandler.createTemplate");

        String templateSql = "INSERT INTO template (title) " +
                "VALUES (?)";

        int templateId;
        try (PreparedStatement statement = getConnection().prepareStatement(templateSql,
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, template.getTitle());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    templateId = resultSet.getInt(1);
                } else {
                    throw new SQLException("Creating record failed, no id obtained");
                }
            }
        }

        String fieldSql = "INSERT INTO template_field (template_id, title, label, type, \"order\") " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = getConnection().prepareStatement(fieldSql)) {
            int order = 0;
            for (Field field : template.getFields()) {
                statement.setInt(1, templateId);
                statement.setString(2, field.getTitle());
                statement.setString(3, field.getLabel());
                statement.setString(4, field.getType().toString());
                statement.setInt(5, order++);
            }
            statement.executeUpdate();
        }

        return templateId;
    }
}
