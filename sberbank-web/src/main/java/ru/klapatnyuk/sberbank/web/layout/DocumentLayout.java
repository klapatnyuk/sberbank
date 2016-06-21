package ru.klapatnyuk.sberbank.web.layout;

import com.vaadin.ui.*;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.web.constant.StyleDimensions;
import ru.klapatnyuk.sberbank.web.constant.StyleNames;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author klapatnyuk
 */
public class DocumentLayout extends VerticalLayout {

    public void clear() {
        removeAllComponents();
    }

    public List<Field> getFields() {
        List<Field> fields = new ArrayList<>();
        Field<Serializable> field;
        for (Component item : this) {
            FieldLayout row = (FieldLayout) item;
            if (isEmptyRow(row)) {
                continue;
            }
            field = new Field<>();
            field.setId(row.getFieldId());
            field.setReferenceId(row.getTemplateFieldId());
            field.setValue(getRowValue(row));
            fields.add(field);
        }
        return fields;
    }

    public void setFields(List<Field> fields) {
        removeAllComponents();
        if (fields != null) {
            // display 1) active fields 2) inactive not empty fields 3) inactive checkboxes with 'true' value
            fields.stream().filter(item ->
                    item.isActive() ||
                            item.getValue() != null &&
                                    !(item.getType() == Field.Type.CHECKBOX && item.getValue().equals(false)))
                    .map(this::newRow).forEach(this::addComponent);
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        FieldLayout row;
        Component field;
        for (Component item : this) {
            row = (FieldLayout) item;
            field = row.getComponent(row.getComponentCount() - 1);
            field.setReadOnly(field.isReadOnly() || readOnly);
        }
    }

    private static boolean isEmptyRow(HorizontalLayout row) {
        AbstractField field = (AbstractField) row.getComponent(row.getComponentCount() - 1);
        if (field instanceof AbstractTextField && ((AbstractTextField) field).getValue().trim().isEmpty()) {
            return true;
        } else if (field instanceof CheckBox && !((CheckBox) field).getValue()) {
            return true;
        }
        return false;
    }

    private static Serializable getRowValue(HorizontalLayout row) {
        AbstractField field = (AbstractField) row.getComponent(row.getComponentCount() - 1);
        if (field instanceof AbstractTextField) {
            return ((AbstractTextField) field).getValue().trim();
        } else if (field instanceof CheckBox) {
            return ((CheckBox) field).getValue();
        }
        return null;
    }

    private FieldLayout newRow(Field field) {

        FieldLayout row = new FieldLayout(field.getReferenceId(), field.getId());
        row.addStyleName(StyleNames.LAYOUT_ROW);
        row.addStyleName(StyleNames.SPACER_TOP);
        row.setSizeFull();

        if (field.getType() == Field.Type.LINE) {
            Label label = new Label(field.getLabel());
            label.setWidth(StyleDimensions.WIDTH_S);
            TextField textField = new TextField();
            textField.setWidth("50%");
            // TODO replace by I18N string
            textField.setInputPrompt("Enter string..");
            textField.setValue(field.getValue() == null ? "" : field.getValue().toString());
            textField.setReadOnly(!field.isActive());

            row.addComponent(label);
            row.addComponent(textField);
            row.setExpandRatio(textField, 1);

        } else if (field.getType() == Field.Type.AREA) {

            Label label = new Label(field.getLabel());
            label.setWidth(StyleDimensions.WIDTH_S);
            TextArea textArea = new TextArea();
            textArea.setWidth("100%");
            textArea.setHeight("100px");
            // TODO replace by I18N string
            textArea.setInputPrompt("Enter text..");
            textArea.setStyleName(StyleNames.BODY_TEXT_AREA);
            textArea.setValue(field.getValue() == null ? "" : field.getValue().toString());
            textArea.setReadOnly(!field.isActive());

            row.addComponent(label);
            row.addComponent(textArea);
            row.setExpandRatio(textArea, 1);

        } else if (field.getType() == Field.Type.CHECKBOX) {
            CheckBox checkBox = new CheckBox(field.getLabel());
            checkBox.setStyleName(StyleNames.SPACER_EMPTY);
            checkBox.setHeight(StyleDimensions.HEIGHT_S);
            checkBox.setValue(field.getValue() == null ? false : (Boolean) field.getValue());
            checkBox.setReadOnly(!field.isActive());

            row.addComponent(checkBox);
            row.setExpandRatio(checkBox, 1);
            row.setComponentAlignment(checkBox, Alignment.MIDDLE_LEFT);
        }

        return row;
    }
}
