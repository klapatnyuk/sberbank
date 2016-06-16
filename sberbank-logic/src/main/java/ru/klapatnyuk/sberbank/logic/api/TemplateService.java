package ru.klapatnyuk.sberbank.logic.api;

import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.entity.Template;

import java.util.Set;

/**
 * @author klapatnyuk
 */
public interface TemplateService extends BusinessService {

    Set<Template> getTemplates();

    Set<Template> getTemplates(boolean active);

    Template getTemplate(int id);

    int createTemplate(Template template);

    boolean updateTemplate(int id, Template template);

    boolean removeTemplate(int id);

    int createField(Field field);

    boolean updateField(int id, Field field);

    boolean moveFieldUp(int id);

    boolean moveFieldDown(int id);

    boolean removeField(int id);
}
