package ru.klapatnyuk.sberbank.logic;

import ru.klapatnyuk.sberbank.logic.api.TemplateService;
import ru.klapatnyuk.sberbank.model.entity.AbstractEntity;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.entity.Template;
import ru.klapatnyuk.sberbank.model.exception.BusinessException;
import ru.klapatnyuk.sberbank.model.handler.FieldHandler;
import ru.klapatnyuk.sberbank.model.handler.TemplateHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

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
            return fieldHandler.findByEntityId(id);
        } catch (SQLException e) {
            throw new BusinessException("Template fields finding error", e);
        }
    }

    @Override
    public void create(Template template) throws BusinessException {
        try {
            int id = templateHandler.create(template);
            fieldHandler.create(id, template.getFields());

        } catch (SQLException e) {
            throw new BusinessException("Template creation error", e);
        }
    }

    @Override
    public void update(Template template) throws BusinessException {
        try {
            // compare edited
            if (templateHandler.compareEdited(template.getId(), template.getEdited()) > 0) {
                throw new SQLException("Concurrency editing detected");
            }

            // update template
            templateHandler.update(template);

            // remove fields
            List<Integer> ids = template.getFields().stream().map(AbstractEntity::getId).collect(Collectors.toList());
            fieldHandler.removeExcept(template.getId(), ids);

            // update fields
            fieldHandler.createOrUpdate(template.getId(), template.getFields());

        } catch (SQLException e) {
            throw new BusinessException("Template edition error", e);
        }
    }

    @Override
    public void remove(int id) throws BusinessException {
        try {
            templateHandler.remove(id);
        } catch (SQLException e) {
            throw new BusinessException("Template removing error", e);
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
