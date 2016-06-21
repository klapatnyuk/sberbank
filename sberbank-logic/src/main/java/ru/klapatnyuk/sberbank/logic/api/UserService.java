package ru.klapatnyuk.sberbank.logic.api;

import ru.klapatnyuk.sberbank.model.entity.User;
import ru.klapatnyuk.sberbank.model.exception.BusinessException;

/**
 * @author klapatnyuk
 */
public interface UserService extends BusinessService {

    User login(String login, String password) throws BusinessException;
}
