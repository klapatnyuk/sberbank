package ru.klapatnyuk.sberbank.model.entity;

import ru.klapatnyuk.sberbank.model.type.api.EnumType;

import java.io.Serializable;

/**
 * @author klapatnyuk
 */
public class Field<M extends Serializable> extends AbstractRemovableEntity {

    private String label;
    private Type type;
    private int index;
    private int related;
    private int referenceId;
    private M value;

    private Field() {
    }

    public static <M extends Serializable> Field<M>.Builder<M> newBuilder() {
        return new Field<M>().new Builder<>();
    }

    public String getLabel() {
        return label;
    }

    public Type getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public int getRelated() {
        return related;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public M getValue() {
        return value;
    }

    /**
     * @author klapatnyuk
     */
    public class Builder<T extends Serializable> extends AbstractRemovableBuilder {

        private Builder() {
        }

        public Builder<T> setLabel(String label) {
            Field.this.label = label;
            return this;
        }

        public Builder<T> setType(Type type) {
            Field.this.type = type;
            return this;
        }

        public Builder<T> setIndex(int index) {
            Field.this.index = index;
            return this;
        }

        public Builder<T> setRelated(int related) {
            Field.this.related = related;
            return this;
        }

        public Builder<T> setReferenceId(int id) {
            Field.this.referenceId = id;
            return this;
        }

        public Builder<T> setValue(M value) {
            Field.this.value = value;
            return this;
        }

        @Override
        public Builder<T> setId(int id) {
            super.setId(id);
            return this;
        }

        @Override
        public Builder<T> setTitle(String title) {
            super.setTitle(title);
            return this;
        }

        @Override
        public Builder<T> setActive(boolean active) {
            super.setActive(active);
            return this;
        }

        @Override
        public Field<M> build() {
            return Field.this;
        }
    }

    /**
     * @author klapatnyuk
     */
    public enum Type implements EnumType {
        LINE,
        AREA,
        CHECKBOX;

        public static Type find(String source) {
            if (source == null || source.isEmpty()) {
                return getDefault();
            }
            for (Type constant : values()) {
                if (constant.toString().toLowerCase().equals(source.toLowerCase())) {
                    return constant;
                }
            }
            return getDefault();
        }

        public static Type getDefault() {
            return LINE;
        }
    }
}
