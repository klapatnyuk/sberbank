package ru.klapatnyuk.sberbank.logic;

import ru.klapatnyuk.sberbank.logic.api.TemplateService;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.entity.Template;
import ru.klapatnyuk.sberbank.model.exception.BusinessException;
import ru.klapatnyuk.sberbank.model.handler.FieldHandler;
import ru.klapatnyuk.sberbank.model.handler.TemplateHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author klapatnyuk
 */
public class TemplateServiceImpl implements TemplateService {

    private final TemplateHandler templateHandler;
    private final FieldHandler fieldHandler;

    public TemplateServiceImpl(TemplateHandler templateHandler, FieldHandler fieldHandler) {
        this.templateHandler = templateHandler;
        this.fieldHandler = fieldHandler;
    }

    @Override
    public List<Template> getAll() throws BusinessException {
        try {
            return templateHandler.findAll();
        } catch (SQLException e) {
            throw new BusinessException("Templates finding error", e);
        }
    }

    @Override
    public List<Field> getFields(int id) throws BusinessException {
        try {
            return fieldHandler.findByTemplateId(id);
        } catch (SQLException e) {
            throw new BusinessException("Template fields finding error", e);
        }
    }

    @Override
    public void setConnection(Connection connection) {
        if (templateHandler != null) {
            templateHandler.setConnection(connection);
        }
        if (fieldHandler != null) {
            fieldHandler.setConnection(connection);
        }
    }
}
