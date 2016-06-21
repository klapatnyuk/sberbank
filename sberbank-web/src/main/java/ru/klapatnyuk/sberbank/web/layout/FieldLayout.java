package ru.klapatnyuk.sberbank.web.layout;

import com.vaadin.ui.HorizontalLayout;

/**
 * @author klapatnyuk
 */
public class FieldLayout extends HorizontalLayout {

    private final int templateFieldId;
    private final int fieldId;

    public FieldLayout(int templateFieldId, int fieldId) {
        this.templateFieldId = templateFieldId;
        this.fieldId = fieldId;
    }

    public int getTemplateFieldId() {
        return templateFieldId;
    }

    public int getFieldId() {
        return fieldId;
    }
}
