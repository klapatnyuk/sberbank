package ru.klapatnyuk.sberbank.model.entity;

import java.util.List;

/**
 * @author klapatnyuk
 */
public class Template extends AbstractRemovableEntity {

    private String title;
    private List<Field> fields;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
