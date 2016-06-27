package ru.klapatnyuk.sberbank.logic;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.klapatnyuk.sberbank.logic.api.UserService;
import ru.klapatnyuk.sberbank.model.entity.User;
import ru.klapatnyuk.sberbank.model.exception.BusinessException;
import ru.klapatnyuk.sberbank.model.handler.UserHandler;

import java.sql.SQLException;

/**
 * @author klapatnyuk
 */
public class UserServiceTest {

    @Test
    public void test() throws SQLException, BusinessException {

        String login = "client";
        String password = "client";

        User user = User.newBuilder()
                .setId(2)
                .setLogin(login)
                .setRole(User.Role.CLIENT)
                .build();

        // build user handler mock

        UserHandler handler = Mockito.mock(UserHandler.class);
        Mockito.when(handler.login(Mockito.anyString(), Mockito.anyString())).thenReturn(user);

        UserService userService = new UserServiceImpl(handler);

        User loggedInUser = userService.login(login, password);
        Assert.assertEquals(login, loggedInUser.getLogin());
        Assert.assertNull(loggedInUser.getPassword());
    }
}
