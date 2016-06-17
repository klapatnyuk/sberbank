package ru.klapatnyuk.sberbank.web;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import ru.klapatnyuk.sberbank.model.entity.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * @author klapatnyuk
 */
public class FieldsLayout extends VerticalLayout {

    private final List<HorizontalLayout> layouts = new ArrayList<>();

    public FieldsLayout() {
        HorizontalLayout row = newRow(null);
        layouts.add(row);
        addComponent(row);
    }

    public void clear() {
        // TODO implement
    }

    public List<Field> getFields() {
        // TODO implement
        return null;
    }

    public void setFields(List<Field> fields) {
        layouts.clear();
        removeAllComponents();
        if (fields != null) {
            fields.stream().map(this::newRow).forEach(item -> {
                layouts.add(item);
                addComponent(item);
            });
        }
        addComponent(newRow(null));
    }

    private HorizontalLayout newRow(Field field) {

        TextField title = new TextField();
        title.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_PROMPT));
        title.setWidth("100%");
        //title.setSizeFull();
        title.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        title.setMaxLength(ValidatePattern.TAGLIKE_STRINGS_LENGTH);
        if (field != null && field.getTitle() != null) {
            title.setValue(field.getTitle());
        }

//        title.addBlurListener(blurListener);
//        title.addTextChangeListener(new VariantTextListener());

        TextField label = new TextField();
        label.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_LABEL_PROMPT));
        label.setWidth("100%");
        //label.setSizeFull();
        label.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        label.setMaxLength(ValidatePattern.TAGLIKE_STRINGS_LENGTH);
        if (field != null && field.getLabel() != null) {
            label.setValue(field.getLabel());
        }

//        label.addBlurListener(blurListener);
//        label.addTextChangeListener(new VariantTextListener());

        ComboBox type = new ComboBox();
        type.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_TYPE_PROMPT));
        type.setWidth("100%");
        type.addItem(Field.Type.LINE);
        type.setItemCaption(Field.Type.LINE, SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_TYPE_LINE));
        type.addItem(Field.Type.AREA);
        type.setItemCaption(Field.Type.AREA, SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_TYPE_AREA));
        type.addItem(Field.Type.CHECKBOX);
        type.setItemCaption(Field.Type.CHECKBOX, SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_TYPE_CHECKBOX));

        Button up = new Button();
        up.setIcon(new ThemeResource("img/close-16x16.png"));
        up.setWidth(StyleDimensions.WIDTH_XXS_BUTTON);
        up.setStyleName(StyleNames.BUTTON_TRANSPARENT);
        up.setDescription(SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_UP));

        Button down = new Button();
        down.setIcon(new ThemeResource("img/close-16x16.png"));
        down.setWidth(StyleDimensions.WIDTH_XXS_BUTTON);
        down.setStyleName(StyleNames.BUTTON_TRANSPARENT);
        down.setDescription(SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_DOWN));

        Button close = new Button();
        close.setIcon(new ThemeResource("img/close-16x16.png"));
        close.setWidth(StyleDimensions.WIDTH_XXS_BUTTON);
        close.setStyleName(StyleNames.BUTTON_TRANSPARENT);
        close.setDescription(SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_CLOSE));

        //        close.addClickListener(event -> removeComponent(row));
//        close.setEnabled(!sequence.isEmpty());

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setWidth("100%");
        buttonsLayout.addComponent(up);
        buttonsLayout.addComponent(down);
        buttonsLayout.addComponent(close);
        buttonsLayout.setExpandRatio(up, 1);
        buttonsLayout.setExpandRatio(down, 1);
        buttonsLayout.setExpandRatio(close, 1);

        HorizontalLayout row = new HorizontalLayout();
        row.setWidth("100%");
        row.addComponent(title);
        row.addComponent(label);
        row.addComponent(type);
        row.addComponent(buttonsLayout);

        row.setExpandRatio(title, 6);
        row.setExpandRatio(label, 6);
        row.setExpandRatio(type, 3);
        row.setExpandRatio(buttonsLayout, 4);

        return row;
    }
}
