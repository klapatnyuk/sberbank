package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klapatnyuk
 */
public class TemplateTab extends AbstractTab implements EditableTab {

    private static final long serialVersionUID = 621247325187983282L;
    private static final Logger LOG = LoggerFactory.getLogger(TemplateTab.class);

    private boolean updated = false;

    //private PollPatternWindow patternWindow;
    private TemplateTabView design;

    public TemplateTab(MenuTab tab, MenuTab actionTab) {
        super(tab, actionTab);
    }

    @Override
    public boolean isUpdated() {
        return updated;
    }

    @Override
    public String getHeader() {
        return SberbankUI.I18N.getString(SberbankKey.Header.H2, SberbankUI.I18N.getString(SberbankKey.Menu.MSGR),
                SberbankUI.I18N.getString(SberbankKey.Header.MSGR_POLL));
    }

    @Override
    public void update() {
        LOG.debug("Tab's update started");
//        design.getRecipientSelect().update();
        updated = true;
    }

    @Override
    public boolean validate() {
        /*List<WarningMessage> messages = new ArrayList<>();

        if (design.getRecipientSelect().getRecipients().isEmpty()) {
            messages.add(new WarningMessage(I18N.getString(MSGR_POLL_RECIPIENT_REQUIRED), design.getRecipientSelect().getComboBox(),
                    getValidationSource()));
        }

        if (design.getStartsField().isEmpty()) {
            messages.add(new WarningMessage(I18N.getString(MSGR_POLL_START_REQUIRED), design.getStartsField(), getValidationSource()));
        }

        if (design.getExpiresField().isEmpty()) {
            messages.add(new WarningMessage(I18N.getString(MSGR_POLL_EXPIRATION_REQUIRED), design.getExpiresField(),
                    getValidationSource()));
        } else if (design.getExpiresField().getValue().compareTo(design.getStartsField().getValue()) < 0) {
            messages.add(new WarningMessage(I18N.getString(MSGR_POLL_PERIOD_EMPTY), design.getExpiresField(), getValidationSource()));
        }

        if (design.getBodyField().getValue().trim().isEmpty()) {
            messages.add(new WarningMessage(I18N.getString(MSGR_POLL_BODY_REQUIRED), design.getBodyField(), getValidationSource()));
        }

        int nonCustomQuantity = design.getChoiceSelect().getStrings().size();
        String customChoice = design.getCustomField().getValue().trim();
        if (nonCustomQuantity < 1 || (nonCustomQuantity == 1 && customChoice.isEmpty())) {
            messages.add(new WarningMessage(I18N.getString(MSGR_POLL_CHOICES_REQUIRED), design.getChoiceSelect().getLastField(),
                    getValidationSource()));
        } else if (design.getChoiceSelect().hasDuplicates()) {
            messages.add(new WarningMessage(I18N.getString(MSGR_POLL_CHOICES_DUPLICATES), design.getChoiceSelect().getFirstDuplicateField(),
                    getValidationSource()));
        } else if (!StringUtils.isEmpty(customChoice) && design.getChoiceSelect().contains(customChoice)) {
            messages.add(new WarningMessage(I18N.getString(MSGR_POLL_CHOICES_DUPLICATES), design.getCustomField(), getValidationSource()));
        }

        if (design.getAllowCustomField().getValue() && design.getCustomField().getValue().trim().isEmpty()) {
            messages.add(new WarningMessage(I18N.getString(MSGR_POLL_CUSTOM_REQUIRED), design.getCustomField(), getValidationSource()));
        }

        SberbankUI.getWarningWindow().addAll(messages);
        return messages.isEmpty();*/
        return false;
    }

    @Override
    public Component getValidationSource() {
        /*return design.getSubmitButton();*/
        return null;
    }

    @Override
    public void clear() {
        /*design.getRecipientSelect().clear();
        design.getExpiresField().clear();
        design.getBodyField().clear();
        design.getChoiceSelect().clear();
        design.getAllowCustomField().clear();
        design.getCustomField().clear();
        design.getAllowMultiplyField().clear();*/
    }

    @Override
    protected void init() {
        super.init();

        /*design.getPatternButton().addClickListener(event -> clickPatternButton());
        design.getAllowCustomField()
                .addValueChangeListener(event -> design.getCustomLayout().setVisible((boolean) event.getProperty().getValue()));
        design.getSubmitButton().addClickListener(event -> clickSubmitButton());*/

        initPatternWindow();
    }

    @Override
    protected TabView getDesign() {
        if (design == null) {
            design = new TemplateTabView();
        }
        return design;
    }

    private void initPatternWindow() {
        /*patternWindow = new PollPatternWindow(I18N.getString(Header.MSGR_POLL_WINDOW_PATTERN));
        patternWindow.getLayout().getOkButton().addClickListener(event -> clickPatternOkButton());*/
    }

    private void clickPatternButton() {
        /*patternWindow.getLayout().update();
        UI.getCurrent().addWindow(patternWindow);*/
    }

    private void clickPatternOkButton() {
        /*patternWindow.close();
        final PollPattern pattern = patternWindow.getLayout().getPattern();
        design.getBodyField().setValue(pattern.getBody());
        design.getChoiceSelect().setStrings(pattern.getAnswers().stream().filter(answer -> !answer.isCustom())
                .map(answer -> answer.getSequence()).collect(Collectors.toList()));
        final PollAnswer customAnswer = pattern.findCustomAnswer();
        design.getAllowCustomField().setValue(customAnswer != null);
        if (customAnswer == null) {
            design.getCustomField().clear();
        } else {
            design.getCustomField().setValue(customAnswer.getSequence());
        }
        design.getAllowMultiplyField().setValue(pattern.isMultiple());*/
    }

    private void clickSubmitButton() {
        /*if (!validate()) {
            return;
        }

        List<ChoiceRequest> choices = design.getChoiceSelect().getChoices();
        if (design.getAllowCustomField().getValue()) {
            String sequence = design.getCustomField().getValue().trim();
            choices.add(new ChoiceRequest((long) choices.size(), sequence, true));
        }

        CreatePollRequest request = new CreatePollRequest();
        request.setAddressees(design.getRecipientSelect().getAddressees());
        request.setStarts(ZonedDateTime.ofInstant(design.getStartsField().getValue().toInstant(), ZoneId.systemDefault()));
        request.setEnds(ZonedDateTime.ofInstant(design.getExpiresField().getValue().toInstant(), ZoneId.systemDefault()).plusDays(1));
        request.setBody(design.getBodyField().getValue().trim());
        request.setQuestion(new QuestionRequest(choices,
                design.getAllowMultiplyField().getValue() ? QuestionType.CHECKBOX : QuestionType.RADIO));

        final PollService pollService = new PollService(BrownieSession.get(), BrownieConfiguration.get());
        CreatePollResponse response;
        try {
            response = pollService.createPoll(request);
        } catch (RemoteServiceException e) {
            BrownieErrorHandler.handle(e, I18N.getString(MSGR_POLL_CREATE_ERROR));
            return;
        }
        LOG.info("New poll created with id: " + response.getId());
        clear();
        Tray.show(I18N.getString(MSGR_POLL_CREATED));*/
    }
}