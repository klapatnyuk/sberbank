package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.AbstractEntity;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.entity.Template;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

                statement.executeUpdate();
            }
        }

        return templateId;
    }

    public void updateTemplate(Template template) throws SQLException {
        LOGGER.debug("Inside TemplateHandler.updateTemplate");

        // check concurrency updating

        String checkEditedSql = "SELECT edited " +
                "FROM template " +
                "WHERE active = TRUE AND id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(checkEditedSql)) {
            statement.setInt(1, template.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Checking concurrency updating failed (record not exists)");
                }
                if (resultSet.getTimestamp(1).toLocalDateTime().compareTo(template.getEdited()) > 0) {
                    throw new SQLException("Concurrency updating detected");
                }
            }
        }

        // update template

        String templateSql = "UPDATE template " +
                "SET title = ?, edited = ? " +
                "WHERE active = TRUE AND id = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(templateSql)) {
            statement.setString(1, template.getTitle());
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(3, template.getId());
            if (statement.executeUpdate() == 0) {
                throw new SQLException("Editing 'template' record failed, no affected records");
            }
        }

        // remove fields

        List<Integer> ids = template.getFields().stream().map(AbstractEntity::getId).collect(Collectors.toList());
        StringBuilder deleteSql = new StringBuilder("UPDATE template_field " +
                "SET active = FALSE " +
                "WHERE active = TRUE AND template_id = ? AND id NOT IN (");
        for (int a = 0; a < ids.size(); a++) {
            if (a > 0) {
                deleteSql.append(", ");
            }
            deleteSql.append("?");
        }
        deleteSql.append(")");
        try (PreparedStatement statement = getConnection().prepareStatement(deleteSql.toString())) {
            statement.setInt(1, template.getId());
            for (int a = 0; a < ids.size(); a++) {
                statement.setInt(a + 2, ids.get(a));
            }
            statement.executeUpdate();
        }

        // update fields

        String insertFieldSql = "INSERT INTO template_field (template_id, title, label, type, \"order\") " +
                "VALUES (?, ?, ?, ?, ?)";
        // TODO check if 'template_id' could change
        String updateFieldSql = "UPDATE template_field " +
                "SET template_id = ?, title = ?, label = ?, type = ?, \"order\" = ? " +
                "WHERE active = TRUE AND id = ?";
        try (PreparedStatement insertStatement = getConnection().prepareStatement(insertFieldSql);
             PreparedStatement updateStatement = getConnection().prepareStatement(updateFieldSql)) {
            int order = 0;
            for (Field field : template.getFields()) {
                if (field.getId() == 0) {
                    // add new fields
                    insertStatement.setInt(1, template.getId());
                    insertStatement.setString(2, field.getTitle());
                    insertStatement.setString(3, field.getLabel());
                    insertStatement.setString(4, field.getType().toString());
                    insertStatement.setInt(5, order++);
                    insertStatement.executeUpdate();
                } else {
                    // edit existed fields
                    updateStatement.setInt(1, template.getId());
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

    public void removeTemplate(int id) throws SQLException {
        String sql = "UPDATE template " +
                "SET active = FALSE " +
                "WHERE active = TRUE AND id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
