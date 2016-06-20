package ru.klapatnyuk.sberbank.model.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author klapatnyuk
 */
public class UserHandler extends AbstractHandler<User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserHandler.class);

    public UserHandler(Connection connection) {
        super(connection);
    }

    public User login(String login, String password) throws SQLException {
        LOGGER.debug("Inside UserHandler.login");

        String sql = "SELECT id, role " +
                "FROM \"user\" " +
                "WHERE login = ? AND password = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, login);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User entity = new User();
                    entity.setId(resultSet.getInt(1));
                    entity.setLogin(login);
                    entity.setRole(User.Role.find(resultSet.getString(2)));
                    return entity;
                }
            }
        }
        return null;
    }
}
