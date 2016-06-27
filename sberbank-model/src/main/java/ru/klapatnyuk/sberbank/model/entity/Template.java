package ru.klapatnyuk.sberbank.model.entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author klapatnyuk
 */
public class Template extends AbstractRemovableEntity {

    private List<Field> fields;
    private LocalDateTime edited;

    Template() {
    }

    public static Builder newBuilder() {
        return new Template().new Builder();
    }

    public List<Field> getFields() {
        return fields;
    }

    public LocalDateTime getEdited() {
        return edited;
    }

    /**
     * @author klapatnyuk
     */
    public class Builder extends AbstractRemovableBuilder {

        Builder() {
        }

        public Builder setFields(List<Field> fields) {
            Template.this.fields = fields;
            return this;
        }

        public Builder setEdited(LocalDateTime edited) {
            Template.this.edited = edited;
            return this;
        }

        @Override
        public Builder setId(int id) {
            super.setId(id);
            return this;
        }

        @Override
        public Builder setTitle(String title) {
            super.setTitle(title);
            return this;
        }

        @Override
        public Builder setActive(boolean active) {
            super.setActive(active);
            return this;
        }

        @Override
        public Template build() {
            return Template.this;
        }
    }
}
