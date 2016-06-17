package ru.klapatnyuk.sberbank.web;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.*;
import com.vaadin.ui.declarative.Design;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author klapatnyuk
 */
@DesignRoot
public class TemplateTabView extends TabView {

    private static final long serialVersionUID = -6788254175139485486L;
    private static final String DATE_FORMAT = SberbankUI.CONFIG.getString(ConfigKey.DATE_FORMAT_SHORT.getKey());

    private Label recipientLabel;
//    private RecipientSelect recipientSelect;
    private Label dateSeparatorLabel;
    private Label startsLabel;
    private DateField startsField;
    private Label expiresLabel;
    private DateField expiresField;
    private Label patternSeparatorLabel;
    private Button patternButton;
    private Label questionLabel;
    private TextArea bodyField;
    private Label choiceLabel;
//    private ChoiceSelect choiceSelect;
    private CheckBox allowCustomField;
    private HorizontalLayout customLayout;
    private Label customLabel;
    private TextField customField;
    private CheckBox allowMultiplyField;
    private Label submitSeparatorLabel;
    private Button submitButton;

    public TemplateTabView() {
        Design.read(this);
        init();
    }

    /*public RecipientSelect getRecipientSelect() {
        return recipientSelect;
    }*/

    public DateField getStartsField() {
        return startsField;
    }

    public DateField getExpiresField() {
        return expiresField;
    }

    public AbstractTextField getBodyField() {
        return bodyField;
    }

    /*public ChoiceSelect getChoiceSelect() {
        return choiceSelect;
    }*/

    public AbstractOrderedLayout getCustomLayout() {
        return customLayout;
    }

    public AbstractField<Boolean> getAllowCustomField() {
        return allowCustomField;
    }

    public AbstractTextField getCustomField() {
        return customField;
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public AbstractField<Boolean> getAllowMultiplyField() {
        return allowMultiplyField;
    }

    public Button getPatternButton() {
        return patternButton;
    }

    @Override
    protected void init() {
        super.init();

        final Date today = Date.from(ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).toInstant());

        recipientLabel.setWidth(StyleDimensions.WIDTH_M);
        recipientLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_RECIPIENT));

        dateSeparatorLabel.setWidth("100%");
        dateSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        startsLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_START));
        startsLabel.setWidth(StyleDimensions.WIDTH_M);
        startsField.setWidth(StyleDimensions.WIDTH);
        startsField.setDateFormat(DATE_FORMAT);
        startsField.setValue(today);
        startsField.setReadOnly(true);

        expiresLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_EXPIRATION));
        expiresLabel.setWidth(StyleDimensions.WIDTH_M);
        expiresField.setWidth(StyleDimensions.WIDTH);
        expiresField.setDateFormat(DATE_FORMAT);
        expiresField.setRangeStart(today);

        patternSeparatorLabel.setWidth("100%");
        patternSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        patternButton.setWidth(StyleDimensions.WIDTH);
        patternButton.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_PATTERN));

        questionLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_QUESTION));
        questionLabel.setWidth(StyleDimensions.WIDTH_M);
        bodyField.setHeight("100px");
        bodyField.setValidationVisible(false);
        bodyField.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_QUESTION_PROMPT));

        choiceLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_CHOICE));
        choiceLabel.setWidth(StyleDimensions.WIDTH_M);

        allowCustomField.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_CUSTOM));
        allowCustomField.setHeight(StyleDimensions.HEIGHT_S);
        customLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_CUSTOM_LABEL));
        customLabel.setWidth(StyleDimensions.WIDTH_M);
        customField.setWidth("100%");
        customField.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_CHOICE_CUSTOM_PROMPT));

        allowMultiplyField.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_MULTIPLY));

        submitSeparatorLabel.setWidth("100%");
        submitSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        submitButton.setWidth(StyleDimensions.WIDTH);
        submitButton.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_SUBMIT));
    }
}
