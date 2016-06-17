package ru.klapatnyuk.sberbank.web;

import com.vaadin.event.FieldEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author klapatnyuk
 */
public class StringSelect extends VerticalLayout {

    private static final long serialVersionUID = 4265108724121009004L;

    private final VariantBlurListener blurListener = new VariantBlurListener();

    public StringSelect() {
        addComponent(newRow(""));
    }

    public boolean hasDuplicates() {
        List<String> list = getStrings();
        if (list.isEmpty()) {
            return false;
        }
        Set<String> set = new HashSet<>(list);
        return set.size() < list.size();
    }

    public boolean contains(String sequence) {
        return getStrings().stream().filter(string -> string.equals(sequence)).findAny().isPresent();
    }

    public void clear() {
        removeAllComponents();
        addComponent(newRow(""));
    }

    public TextField getLastField() {
        return (TextField) ((HorizontalLayout) getComponent(getComponentCount() - 1)).getComponent(0);
    }

    public TextField getFirstDuplicateField() {
        List<String> list = getStrings();
        if (list.isEmpty()) {
            return null;
        }
        List<String> unique = new ArrayList<>();
        for (String sequence : list) {
            if (unique.contains(sequence)) {
                boolean found = false;
                Iterator<Component> iterator = iterator();
                while (iterator.hasNext()) {
                    HorizontalLayout row = (HorizontalLayout) iterator.next();
                    TextField field = (TextField) row.getComponent(0);
                    String target = field.getValue().trim();
                    if (target.equals(sequence)) {
                        if (found) {
                            return field;
                        } else {
                            found = true;
                        }
                    }
                }
            } else {
                unique.add(sequence);
            }
        }
        return null;
    }

    public List<String> getStrings() {
        return StreamSupport.stream(spliterator(), false)
                .map(component -> ((TextField) ((HorizontalLayout) component).getComponent(0)).getValue().trim())
                .filter(sequence -> !sequence.isEmpty()).collect(Collectors.toList());
    }

    public Set<String> getUniqueStrings() {
        return new HashSet<>(getStrings());
    }

    public void setStrings(Collection<String> sequences) {
        removeAllComponents();
        if (sequences != null && !sequences.isEmpty()) {
            sequences.forEach(sequence -> addComponent(newRow(sequence)));
        }
        addComponent(newRow(""));
    }

    private HorizontalLayout newRow(String sequence) {
        HorizontalLayout row = new HorizontalLayout();

        TextField field = new TextField();
        field.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_PROMPT));
        field.setWidth("100%");
        field.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        field.setMaxLength(ValidatePattern.TAGLIKE_STRINGS_LENGTH);
        if (!sequence.isEmpty()) {
            field.setValue(sequence);
        }

        field.addBlurListener(blurListener);
        field.addTextChangeListener(new VariantTextListener());

        Button button = new Button();
        button.setIcon(new ThemeResource("img/close-16x16.png"));
        button.setWidth(StyleDimensions.WIDTH_XXS);
        button.setStyleName(StyleNames.BUTTON_TRANSPARENT);
        button.addClickListener(event -> removeComponent(row));
        button.setEnabled(!sequence.isEmpty());
        button.setDescription(SberbankUI.I18N.getString(SberbankKey.Form.STRINGS_CLOSE));

        row.setWidth("50%");
        row.addComponent(field);
        row.setExpandRatio(field, 1);
        row.addComponent(button);

        return row;
    }

    /**
     * @author vykla
     */
    private class VariantBlurListener implements FieldEvents.BlurListener {

        private static final long serialVersionUID = -848704378272173089L;

        @Override
        public void blur(FieldEvents.BlurEvent event) {
            TextField current = (TextField) event.getSource();
            if (current.getValue().trim().isEmpty() && hasTwoEmptyRows()) {
                removeComponent(current.getParent());
            }
        }

        private boolean hasTwoEmptyRows() {
            boolean found = false;
            Iterator<Component> iterator = iterator();
            while (iterator.hasNext()) {
                HorizontalLayout row = (HorizontalLayout) iterator.next();
                TextField field = (TextField) row.getComponent(0);
                if (field.getValue().trim().isEmpty()) {
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

    /**
     * @author vykla
     */
    private class VariantTextListener implements FieldEvents.TextChangeListener {

        private static final long serialVersionUID = 7246934444356053996L;

        private HorizontalLayout row;

        @Override
        public void textChange(FieldEvents.TextChangeEvent event) {
            if (isLastRow(event) && !event.getText().trim().isEmpty() && row == null) {
                row = newRow("");
                addComponent(row);
                TextField textField = (TextField) event.getSource();
                HorizontalLayout row = (HorizontalLayout) textField.getParent();
                row.getComponent(1).setEnabled(true);
            }
        }

        private boolean isLastRow(FieldEvents.TextChangeEvent event) {
            HorizontalLayout currentRow = (HorizontalLayout) event.getComponent().getParent();
            return getComponentIndex(currentRow) == getComponentCount() - 1;
        }
    }
}
