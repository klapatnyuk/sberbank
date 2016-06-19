package ru.klapatnyuk.sberbank.model.entity;

import ru.klapatnyuk.sberbank.model.type.api.EnumType;

import java.io.Serializable;

/**
 * @author klapatnyuk
 */
public class Field<T extends Serializable> extends AbstractRemovableEntity {

    private String label;
    private Type type;
    private int order;
    private int referenceId;
    private T value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int id) {
        this.referenceId = id;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
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
