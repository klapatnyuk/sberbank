package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.Template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
