package ru.klapatnyuk.sberbank.model.entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author klapatnyuk
 */
public class Template extends AbstractRemovableEntity {

    private String title;
    private List<Field> fields;
    private LocalDateTime edited;

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

    public LocalDateTime getEdited() {
        return edited;
    }

    public void setEdited(LocalDateTime edited) {
        this.edited = edited;
    }
}
