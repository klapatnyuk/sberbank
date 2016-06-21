package ru.klapatnyuk.sberbank.logic.api;

import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.entity.Template;
import ru.klapatnyuk.sberbank.model.exception.BusinessException;

import java.util.List;

/**
 * @author klapatnyuk
 */
public interface TemplateService extends BusinessService {

    List<Template> getAll() throws BusinessException;

    List<Field> getFields(int id) throws BusinessException;

    void create(Template template) throws BusinessException;

    void update(Template template) throws BusinessException;

    void remove(int id) throws BusinessException;
}
