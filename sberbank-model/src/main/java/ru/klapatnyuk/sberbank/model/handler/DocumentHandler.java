package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.Document;
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
}
