package ru.klapatnyuk.sberbank.logic.api;

import ru.klapatnyuk.sberbank.model.entity.Document;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.entity.User;
import ru.klapatnyuk.sberbank.model.exception.BusinessException;

import java.util.List;

/**
 * @author klapatnyuk
 */
public interface DocumentService extends BusinessService {

    List<Document> getAll() throws BusinessException;

    List<Document> get(User owner) throws BusinessException;

    List<Field> getFields(int id) throws BusinessException;

    void create(Document document) throws BusinessException;

    void update(Document document) throws BusinessException;

    void remove(int id) throws BusinessException;
}
