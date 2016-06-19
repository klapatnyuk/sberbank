package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.*;
import ru.klapatnyuk.sberbank.model.entity.Field;

import java.util.List;

/**
 * @author klapatnyuk
 */
public class DocumentLayout extends VerticalLayout {

    public void clear() {
        removeAllComponents();
    }

    public List<Field> getFields() {
        // TODO implement
        return null;
    }

    public void setFields(List<Field> fields) {
        removeAllComponents();
        if (fields != null) {
            fields.stream().filter(item -> item.isActive() || item.getValue() != null).map(this::newRow)
                    .forEach(this::addComponent);
        }
    }

    private FieldLayout newRow(Field field) {

        FieldLayout row = new FieldLayout(field.getReferenceId(), field.getId());
        row.addStyleName(StyleNames.LAYOUT_ROW);
        row.addStyleName(StyleNames.SPACER_TOP);
        row.setSizeFull();

        if (field.getType() == Field.Type.LINE) {
            Label label = new Label(field.getLabel());
            label.setWidth(StyleDimensions.WIDTH_S);
            label.setEnabled(field.isActive());
            TextField textField = new TextField();
            textField.setWidth("50%");
            textField.setInputPrompt("Enter string..");
            textField.setEnabled(field.isActive());
            textField.setValue(field.getValue() == null ? "" : field.getValue().toString());

            row.addComponent(label);
            row.addComponent(textField);
            row.setExpandRatio(textField, 1);

        } else if (field.getType() == Field.Type.AREA) {

            Label label = new Label(field.getLabel());
            label.setWidth(StyleDimensions.WIDTH_S);
            label.setEnabled(field.isActive());
            TextArea textArea = new TextArea();
            textArea.setWidth("100%");
            textArea.setHeight("100px");
            textArea.setInputPrompt("Enter text..");
            textArea.setEnabled(field.isActive());
            textArea.setStyleName("body-text-area");
            textArea.setValue(field.getValue() == null ? "" : field.getValue().toString());

            row.addComponent(label);
            row.addComponent(textArea);
            row.setExpandRatio(textArea, 1);

        } else if (field.getType() == Field.Type.CHECKBOX) {
            CheckBox checkBox = new CheckBox(field.getLabel());
            checkBox.setEnabled(field.isActive());
            checkBox.setStyleName("spacer-empty");
            checkBox.setHeight(StyleDimensions.HEIGHT_S);
            checkBox.setValue(field.getValue() == null ? false : (Boolean) field.getValue());

            row.addComponent(checkBox);
            row.setExpandRatio(checkBox, 1);
            row.setComponentAlignment(checkBox, Alignment.MIDDLE_LEFT);
        }

        return row;
    }
}
