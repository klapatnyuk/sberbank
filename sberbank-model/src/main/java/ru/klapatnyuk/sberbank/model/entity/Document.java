package ru.klapatnyuk.sberbank.model.entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author klapatnyuk
 */
public class Document extends Template {

    private Template template;
    private User owner;

    private Document() {
    }

    public static Builder newBuilder() {
        return new Document().new Builder();
    }

    public Template getTemplate() {
        return template;
    }

    public User getOwner() {
        return owner;
    }

    /**
     * @author klapatnyuk
     */
    public class Builder extends Template.Builder {

        private Builder() {
        }

        public Builder setTemplate(Template template) {
            Document.this.template = template;
            return this;
        }

        public Builder setOwner(User owner) {
            Document.this.owner = owner;
            return this;
        }

        @Override
        public Builder setFields(List<Field> fields) {
            super.setFields(fields);
            return this;
        }

        @Override
        public Builder setEdited(LocalDateTime edited) {
            super.setEdited(edited);
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
        public Document build() {
            return Document.this;
        }
    }
}
