package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.Field;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author klapatnyuk
 */
public class TemplateFieldHandler extends FieldHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateFieldHandler.class);
    private static final String TABLE = "template_field";

    public List<Field> findByTemplateId(int id) throws SQLException {
        LOGGER.debug("Entering findByTemplateId(" + id + ")");

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

    public void createTemplateFields(int templateId, List<Field> fields) throws SQLException {
        LOGGER.debug("Entering createTemplateFields(" + templateId + ", List<Field>)");
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

    public void removeTemplateFieldsExcept(int templateId, List<Integer> ids) throws SQLException {
        LOGGER.debug("Entering removeTemplateFieldsExcept(" + templateId + ", " + ids + ")");
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

    public void insertAndUpdateTemplateFields(int templateId, List<Field> fields) throws SQLException {
        LOGGER.debug("Entering insertAndUpdateTemplateFields(" + templateId + ", List<Field>)");

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

    @Override
    protected String getTable() {
        return TABLE;
    }
}
