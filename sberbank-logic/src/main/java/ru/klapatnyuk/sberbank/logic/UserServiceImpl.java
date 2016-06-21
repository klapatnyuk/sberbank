package ru.klapatnyuk.sberbank.logic;

import ru.klapatnyuk.sberbank.logic.api.UserService;
import ru.klapatnyuk.sberbank.model.entity.User;
import ru.klapatnyuk.sberbank.model.exception.BusinessException;
import ru.klapatnyuk.sberbank.model.handler.UserHandler;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author klapatnyuk
 */
public class UserServiceImpl implements UserService {

    private final UserHandler userHandler;

    public UserServiceImpl(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Override
    public User login(String login, String password) throws BusinessException {
        try {
            return userHandler.login(login, password);
        } catch (SQLException e) {
            throw new BusinessException("Login error", e);
        }
    }

    @Override
    public void setConnection(Connection connection) {
        if (userHandler != null) {
            userHandler.setConnection(connection);
        }
    }
}
