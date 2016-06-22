package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.api.Entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author klapatnyuk
 */
public abstract class EditableEntityHandler<T extends Entity> extends AbstractHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditableEntityHandler.class);

    public void update(T entity) throws SQLException {
        LOGGER.debug("Entering update");

        String sql = "UPDATE " + getTable() + " " +
                "SET title = ?, edited = ? " +
                "WHERE active = TRUE AND id = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, entity.getTitle());
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(3, entity.getId());
            if (statement.executeUpdate() == 0) {
                throw new SQLException("Editing entity record failed, no affected records");
            }
        }
    }


    public int compareEdited(int id, LocalDateTime edited) throws SQLException {
        LOGGER.debug("Entering compareEdited(" + id + ", " + edited + ")");

        String sql = "SELECT edited " +
                "FROM " + getTable() + " " +
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

    public void remove(int id) throws SQLException {
        LOGGER.debug("Entering remove(" + id + ")");

        String sql = "UPDATE " + getTable() + " " +
                "SET active = FALSE " +
                "WHERE active = TRUE AND id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
