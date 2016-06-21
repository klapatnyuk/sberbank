package ru.klapatnyuk.sberbank.web.layout;

import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.web.*;
import ru.klapatnyuk.sberbank.web.constant.StyleDimensions;
import ru.klapatnyuk.sberbank.web.constant.StyleNames;
import ru.klapatnyuk.sberbank.web.constant.ValidatePattern;

import java.util.*;

/**
 * @author klapatnyuk
 */
public class TemplateLayout extends VerticalLayout {

    private static final int TITLE = 0;
    private static final int LABEL = 1;
    private static final int TYPE = 2;
    private static final int BUTTONS = 3;

    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int REMOVE = 2;

    private final List<FieldLayout> layouts = new ArrayList<>();
    private final BlurListener blurListener = new BlurListener();
    private final UpListener upListener = new UpListener();
    private final DownListener downListener = new DownListener();

    public TemplateLayout() {
        addComponent(newRow(null));
    }

    public void clear() {
        removeAllComponents();
        addComponent(newRow(null));
    }

    public boolean isEmpty() {
        for (Component item : this) {
            if (!isEmptyRow((HorizontalLayout) item)) {
                return false;
            }
        }
        return true;
    }

    public List<Field> getFields() {
        List<Field> fields = new ArrayList<>();
        Field field;
        for (Component item : this) {
            FieldLayout row = (FieldLayout) item;
            if (isEmptyRow(row)) {
                continue;
            }
            field = new Field();
            field.setId(row.getTemplateFieldId());
            field.setTitle(((TextField) row.getComponent(TITLE)).getValue().trim());
            field.setLabel(((TextField) row.getComponent(LABEL)).getValue().trim());
            field.setType((Field.Type) ((ComboBox) row.getComponent(TYPE)).getValue());
            fields.add(field);
        }
        return fields;
    }

    public void setFields(List<Field> fields) {
        removeAllComponents();
        if (fields != null) {
            fields.stream().map(this::newRow).forEach(this::addComponent);
        }
        addComponent(newRow(null));
    }

    public AbstractField getEmptyRowsField() {
        Iterator<Component> iterator = iterator();
        HorizontalLayout row;
        while (iterator.hasNext()) {
            row = (HorizontalLayout) iterator.next();
            if (isEmptyRow(row)) {
                return (AbstractField) row.getComponent(TITLE);
            }
        }
        return null;
    }

    /**
     * Searches duplicates only among 'title' not empty field
     */
    public boolean hasDuplicates() {
        List<String> all = new ArrayList<>();
        forEach(item -> {
            String value = ((TextField) ((HorizontalLayout) item).getComponent(TITLE)).getValue().trim();
            if (!value.isEmpty()) {
                all.add(value);
            }
        });
        return new HashSet<>(all).size() < all.size();
    }

    /**
     * Searches duplicates only among 'title' not empty field
     */
    public AbstractField getFirstDuplicateField() {
        Set<String> unique = new HashSet<>();
        TextField field;
        String value;
        for (Component item : this) {
            field = (TextField) ((HorizontalLayout) item).getComponent(TITLE);
            value = field.getValue().trim();
            if (value.isEmpty()) {
                continue;
            }
            if (unique.contains(value)) {
                return field;
            } else {
                unique.add(value);
            }
        }
        return null;
    }

    /**
     * Searches empty among all 'not empty row' fields
     */
    public AbstractField getFirstEmptyField() {
        for (Component rowItem : this) {
            HorizontalLayout row = (HorizontalLayout) rowItem;
            if (isEmptyRow(row)) {
                continue;
            }
            if (((TextField) row.getComponent(TITLE)).getValue().trim().isEmpty()) {
                return (AbstractField) row.getComponent(TITLE);
            }
            if (((TextField) row.getComponent(LABEL)).getValue().trim().isEmpty()) {
                return (AbstractField) row.getComponent(LABEL);
            }
            if (((ComboBox) row.getComponent(TYPE)).isEmpty()) {
                return (AbstractField) row.getComponent(TYPE);
            }
        }
        return null;
    }

    @Override
    public void addComponent(Component component) {
        FieldLayout row = (FieldLayout) component;

        // add row
        layouts.add(row);
        super.addComponent(row);

        // update only neighbors navigate buttons
        if (layouts.size() <= 2) {
            return;
        }
        int index = layouts.indexOf(row);
        ((HorizontalLayout) layouts.get(index - 2).getComponent(BUTTONS)).getComponent(DOWN).setEnabled(true);
        ((HorizontalLayout) layouts.get(index - 1).getComponent(BUTTONS)).getComponent(UP).setEnabled(true);
    }

    @Override
    public void addComponent(Component component, int index) {
        FieldLayout row = (FieldLayout) component;

        // add row
        layouts.add(index, row);
        super.addComponent(row, index);

        // update navigate buttons
        if (layouts.size() <= 2) {
            return;
        }
        // self
        ((HorizontalLayout) row.getComponent(BUTTONS)).getComponent(UP).setEnabled(index > 0);
        ((HorizontalLayout) row.getComponent(BUTTONS)).getComponent(DOWN).setEnabled(index < layouts.size() - 2);

        // neighbors
        if (index > 0 && index == layouts.size() - 2) {
            ((HorizontalLayout) layouts.get(index - 1).getComponent(BUTTONS)).getComponent(DOWN).setEnabled(true);
        }
        if (index == 0) {
            ((HorizontalLayout) layouts.get(index + 1).getComponent(BUTTONS)).getComponent(UP).setEnabled(true);
        }
    }

    @Override
    public void removeComponent(Component component) {
        FieldLayout row = (FieldLayout) component;

        // update only neighbors navigate buttons
        if (layouts.size() > 2) {
            int index = layouts.indexOf(row);
            if (index > 0 && index == layouts.size() - 2) {
                ((HorizontalLayout) layouts.get(index - 1).getComponent(BUTTONS)).getComponent(DOWN).setEnabled(false);
            }
            if (index == 0) {
                ((HorizontalLayout) layouts.get(index + 1).getComponent(BUTTONS)).getComponent(UP).setEnabled(false);
            }
        }

        // remove row
        layouts.remove(row);
        super.removeComponent(row);
    }

    @Override
    public void removeAllComponents() {
        layouts.clear();
        super.removeAllComponents();
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

    private FieldLayout newRow(Field field) {

        TextField title = new TextField();
        title.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_PROMPT));
        title.setWidth("100%");
        title.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        title.setMaxLength(ValidatePattern.TAGLIKE_STRINGS_LENGTH);

        TextField label = new TextField();
        label.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_LABEL_PROMPT));
        label.setWidth("100%");
        label.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        label.setMaxLength(ValidatePattern.TAGLIKE_STRINGS_LENGTH);

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
        up.setEnabled(false);
        up.setIcon(new ThemeResource("img/close-16x16.png"));
        up.setWidth(StyleDimensions.WIDTH_XXS_BUTTON);
        up.setStyleName(StyleNames.BUTTON_TRANSPARENT);
        up.setDescription(SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_UP));

        Button down = new Button();
        down.setEnabled(false);
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

        FieldLayout row = new FieldLayout(field == null ? 0 : field.getId(), 0);
        row.setWidth("100%");
        row.addComponent(title);
        row.addComponent(label);
        row.addComponent(type);
        row.addComponent(buttonsLayout);

        row.setExpandRatio(title, 6);
        row.setExpandRatio(label, 6);
        row.setExpandRatio(type, 3);
        row.setExpandRatio(buttonsLayout, 4);

        // update values
        if (field != null) {
            if (field.getTitle() != null) {
                title.setValue(field.getTitle());
            }
            if (field.getLabel() != null) {
                label.setValue(field.getLabel());
            }
            if (field.getType() != null) {
                type.setValue(field.getType());
            }
            type.setReadOnly(field.getRelated() > 0);
            remove.setEnabled(true);
        }

        // add listeners

        ValueChangeListener valueListener = new ValueChangeListener(row);
        title.addTextChangeListener(valueListener);
        label.addTextChangeListener(valueListener);
        type.addValueChangeListener(valueListener);

        title.addBlurListener(blurListener);
        label.addBlurListener(blurListener);
        type.addBlurListener(blurListener);

        up.addClickListener(upListener);
        down.addClickListener(downListener);
        remove.addClickListener(event -> removeComponent(row));

        return row;
    }

    /**
     * @author klapatnyuk
     */
    private class UpListener implements Button.ClickListener {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            FieldLayout row = (FieldLayout) ((Component) event.getSource()).getParent().getParent();
            int index = layouts.indexOf(row);
            if (index < 1) {
                return;
            }
            removeComponent(row);
            addComponent(row, index - 1);
        }
    }

    /**
     * @author klapatnyuk
     */
    private class DownListener implements Button.ClickListener {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            FieldLayout row = (FieldLayout) ((Component) event.getSource()).getParent().getParent();
            int index = layouts.indexOf(row);
            if (index > layouts.size() - 3) {
                return;
            }
            removeComponent(row);
            addComponent(row, index + 1);
        }
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
