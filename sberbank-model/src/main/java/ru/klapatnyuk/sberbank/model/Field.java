package ru.klapatnyuk.sberbank.model;

import ru.klapatnyuk.sberbank.model.api.EnumType;

import java.io.Serializable;

/**
 * @author klapatnyuk
 */
public class Field<T extends Serializable> extends AbstractRemovableEntity {

    private String title;
    private String label;
    private Type type;
    private int order;
    private T value;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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
