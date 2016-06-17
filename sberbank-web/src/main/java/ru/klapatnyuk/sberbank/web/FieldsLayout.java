package ru.klapatnyuk.sberbank.web;

import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import ru.klapatnyuk.sberbank.model.entity.Field;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author klapatnyuk
 */
public class FieldsLayout extends VerticalLayout {

    private static final int TITLE = 0;
    private static final int LABEL = 1;
    private static final int TYPE = 2;
    private static final int BUTTONS = 3;

    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int REMOVE = 2;

    private final List<HorizontalLayout> layouts = new ArrayList<>();
    private final BlurListener blurListener = new BlurListener();

    public FieldsLayout() {
        HorizontalLayout row = newRow(null);
        layouts.add(row);
        addComponent(row);

//        // second empty line added for usability
//        row = newRow(null);
//        layouts.add(row);
//        addComponent(row);
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

    private static boolean isEmptyRow(HorizontalLayout row, FieldEvents.TextChangeEvent event) {
        if (!((ComboBox) row.getComponent(TYPE)).isEmpty() || !event.getText().trim().isEmpty()) {
            return false;
        }

        switch (((AbstractOrderedLayout) row.getParent()).getComponentIndex((Component) event.getSource())) {
            case TITLE:
                return ((TextField) row.getComponent(LABEL)).getValue().trim().isEmpty();
            case LABEL:
                return ((TextField) row.getComponent(TITLE)).getValue().trim().isEmpty();
        }

        // to prevent uncontrollable row addition
        return true;
    }

    private static boolean isEmptyRow(HorizontalLayout row) {
        return ((TextField) row.getComponent(TITLE)).getValue().trim().isEmpty() &&
                ((TextField) row.getComponent(LABEL)).getValue().trim().isEmpty() &&
                ((ComboBox) row.getComponent(TYPE)).isEmpty();
    }

    private static boolean isLastRow(HorizontalLayout row) {
        AbstractOrderedLayout parent = (AbstractOrderedLayout) row.getParent();
        return parent.getComponentIndex(row) == parent.getComponentCount() - 1;
    }

    private HorizontalLayout newRow(Field field) {

        TextField title = new TextField();
        title.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_PROMPT));
        title.setWidth("100%");
        title.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        title.setMaxLength(ValidatePattern.TAGLIKE_STRINGS_LENGTH);
        if (field != null && field.getTitle() != null) {
            title.setValue(field.getTitle());
        }

        TextField label = new TextField();
        label.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_LABEL_PROMPT));
        label.setWidth("100%");
        label.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        label.setMaxLength(ValidatePattern.TAGLIKE_STRINGS_LENGTH);
        if (field != null && field.getLabel() != null) {
            label.setValue(field.getLabel());
        }

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

        Button remove = new Button();
        remove.setEnabled(false);
        remove.setIcon(new ThemeResource("img/close-16x16.png"));
        remove.setWidth(StyleDimensions.WIDTH_XXS_BUTTON);
        remove.setStyleName(StyleNames.BUTTON_TRANSPARENT);
        remove.setDescription(SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_CLOSE));

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setWidth("100%");
        buttonsLayout.addComponent(up);
        buttonsLayout.addComponent(down);
        buttonsLayout.addComponent(remove);
        buttonsLayout.setExpandRatio(up, 1);
        buttonsLayout.setExpandRatio(down, 1);
        buttonsLayout.setExpandRatio(remove, 1);

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

        // add listeners

        ValueChangeListener valueListener = new ValueChangeListener(row);
        title.addTextChangeListener(valueListener);
        label.addTextChangeListener(valueListener);
        type.addValueChangeListener(valueListener);

        title.addBlurListener(blurListener);
        label.addBlurListener(blurListener);
        type.addBlurListener(blurListener);

        remove.addClickListener(event -> removeComponent(row));

        return row;
    }

    /**
     * @author klapatnyuk
     */
    private class ValueChangeListener implements FieldEvents.TextChangeListener, Property.ValueChangeListener {

        private static final long serialVersionUID = 7246934444356053996L;

        private final HorizontalLayout row;

        private HorizontalLayout nextRow;

        public ValueChangeListener(HorizontalLayout row) {
            this.row = row;
        }

        @Override
        public void textChange(FieldEvents.TextChangeEvent event) {
            boolean empty = isEmptyRow(row, event);

            // set current row removable
            ((HorizontalLayout) row.getComponent(BUTTONS)).getComponent(REMOVE).setEnabled(!empty);

            // add next empty row
            if (!empty) {
                addNextRow();
            }
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            boolean empty = isEmptyRow(row);

            // set current row removable
            ((HorizontalLayout) row.getComponent(BUTTONS)).getComponent(REMOVE).setEnabled(!empty);

            // add next empty row
            if (!empty) {
                addNextRow();
            }
        }

        private void addNextRow() {
            if (!isLastRow(row) || nextRow != null) {
                return;
            }
            nextRow = newRow(null);
            layouts.add(nextRow);
            addComponent(nextRow);
        }
    }

    /**
     * @author vykla
     */
    private class BlurListener implements FieldEvents.BlurListener {

        private static final long serialVersionUID = -848704378272173089L;

        @Override
        public void blur(FieldEvents.BlurEvent event) {
            if (!hasTwoEmptyRows()) {
                return;
            }
            AbstractField field = (AbstractField) event.getSource();
            if ((field instanceof TextField && ((String) field.getValue()).trim().isEmpty()) ||
                    (field instanceof ComboBox && field.isEmpty())) {
                removeComponent(field.getParent());
            }
        }

        private boolean hasTwoEmptyRows() {
            boolean found = false;
            Iterator<Component> iterator = iterator();
            while (iterator.hasNext()) {
                if (isEmptyRow((HorizontalLayout) iterator.next())) {
                    if (found) {
                        return true;
                    } else {
                        found = true;
                    }
                }
            }
            return false;
        }
    }
}
