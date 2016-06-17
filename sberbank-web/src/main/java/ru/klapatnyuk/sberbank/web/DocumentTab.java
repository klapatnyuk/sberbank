package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.toggle.ButtonGroupSelectionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author klapatnyuk
 */
public class DocumentTab extends AbstractTab implements EditableTab {

    private static final long serialVersionUID = 5490116135419202151L;
    private static final int LENGTH = Integer.parseInt(SberbankUI.CONFIG.getString(ConfigKey.PATTERN_SUBJECT_LENGTH.getKey()));
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentTab.class);

    private Integer patternIndex = -1;
//    private PollPattern pattern;
    private boolean updated = false;

    private org.vaadin.addons.toggle.ButtonGroup buttonGroup = new org.vaadin.addons.toggle.ButtonGroup();
    private DocumentTabView design;

    public DocumentTab(MenuTab tab, MenuTab actionTab) {
        super(tab, actionTab);
    }

    @Override
    public boolean isUpdated() {
        return updated;
    }

    @Override
    public void update() {
        /*buttonGroup = new org.vaadin.addons.toggle.ButtonGroup();
        design.getEditPatternLayout().removeAllComponents();
        if (BrownieSession.getMasterdata().getPolls() == null) {
            return;
        }
        BrownieSession.getMasterdata().getPolls().stream().map(PollPattern::getBody).forEach(body -> {
            Button button = new Button((body.length() > LENGTH) ? body.substring(0, LENGTH) + ".." : body);
            button.setDescription(body);
            button.setWidth("100%");
            button.addStyleName("pattern-button");
            button.addClickListener(event -> clickPatternButton(event));
            buttonGroup.addButton(button);
            design.getEditPatternLayout().addComponent(button);
        });
        final boolean nonEmpty = buttonGroup.getButtons().length > 0;
        design.getEditSeparatorLabel().setVisible(nonEmpty);
        design.getEditLabel().setVisible(nonEmpty);
        design.getPatternContainer().setVisible(nonEmpty);
        if (nonEmpty) {
            buttonGroup.addSelectionListener(event -> selectPattern(event));
            if (patternIndex >= 0) {
                buttonGroup.setSelectedButtonIndex(patternIndex);
                selectPattern(new ButtonGroupSelectionEvent(buttonGroup, buttonGroup.getButtons()[patternIndex], null));
            } else if (folder != null) {
                design.getCurrentFolderLabel().setValue(folder.getTitle());
            }
        }
        updated = true;*/
    }

    @Override
    public String getHeader() {
        return SberbankUI.I18N.getString(SberbankKey.Header.H2, SberbankUI.I18N.getString(SberbankKey.Menu.MSGR),
                SberbankUI.I18N.getString(SberbankKey.Header.PTRN_POLL));
    }

    @Override
    public boolean validate() {
        List<WarningMessage> messages = new ArrayList<>();

        if (design.getBodyField().getValue().trim().isEmpty()) {
            messages.add(new WarningMessage(SberbankUI.I18N.getString(SberbankKey.Notification.PTRN_POLL_BODY_VALIDATE),
                    design.getBodyField(), getValidationSource()));
        }

        if (design.getChoiceSelect().getStrings().size() < 2) {
            messages.add(new WarningMessage(SberbankUI.I18N.getString(SberbankKey.Notification.PTRN_POLL_CHOICES_REQUIRED),
                    design.getChoiceSelect().getLastField(),
                    getValidationSource()));
        } else if (design.getChoiceSelect().hasDuplicates()) {
            messages.add(new WarningMessage(SberbankUI.I18N.getString(SberbankKey.Notification.PTRN_POLL_CHOICES_DUPLICATES),
                    design.getChoiceSelect().getFirstDuplicateField(),
                    getValidationSource()));
        }

        if (design.getAllowCustomField().getValue() && design.getCustomField().getValue().trim().isEmpty()) {
            messages.add(new WarningMessage(SberbankUI.I18N.getString(SberbankKey.Notification.PTRN_POLL_CUSTOM_REQUIRED),
                    design.getCustomField(), getValidationSource()));
        }

        SberbankUI.getWarningWindow().addAll(messages);
        return messages.isEmpty();
    }

    @Override
    public Component getValidationSource() {
        return design.getSubmitButton();
    }

    @Override
    public void clear() {
        design.getBodyField().clear();
        design.getChoiceSelect().clear();
        design.getAllowCustomField().clear();
        design.getCustomField().clear();
        design.getAllowMultiplyField().clear();
        design.getTagSelect().clear();
    }

    @Override
    protected void init() {
        super.init();

        design.getCreateButton().addClickListener(event -> clickCreateButton());
        design.getAllowCustomField()
                .addValueChangeListener(event -> design.getCustomLayout().setVisible((boolean) event.getProperty().getValue()));
        design.getSubmitButton().addClickListener(event -> clickSubmitButton());
    }

    @Override
    protected TabView getDesign() {
        if (design == null) {
            design = new DocumentTabView();
        }
        return design;
    }

    private void selectPattern(ButtonGroupSelectionEvent event) {
        /*if (event.getPreviousButton() != null) {
            event.getPreviousButton().removeStyleName(StyleNames.BUTTON_ACTIVE);
        }
        design.getCreateButton().removeStyleName(StyleNames.BUTTON_ACTIVE);
        event.getSelectedButton().addStyleName(StyleNames.BUTTON_ACTIVE);
        design.getSubmitButton().setCaption(SberbankUI.I18N.getString(PTRN_POLL_SAVE));
        patternIndex = buttonGroup.indexOfButton(buttonGroup.getSelectedButton());
        pattern = BrownieSession.getMasterdata().getPolls().get(patternIndex);
        design.getBodyField().setValue(pattern.getBody());
        design.getChoiceSelect().setStrings(pattern.getAnswers().stream().filter(answer -> !answer.isCustom()).map(PollAnswer::getSequence)
                .collect(Collectors.toList()));
        final PollAnswer customAnswer = pattern.findCustomAnswer();
        design.getAllowCustomField().setValue(customAnswer != null);
        if (customAnswer == null) {
            design.getCustomField().clear();
        } else {
            design.getCustomField().setValue(customAnswer.getSequence());
        }
        design.getAllowMultiplyField().setValue(pattern.isMultiple());
        folder = pattern.getFolder();
        design.getCurrentFolderLabel().setValue(folder.getTitle());
        design.getTagSelect().setStrings(pattern.getTags());*/
    }

    private void clickPatternButton(Button.ClickEvent event) {
        /*final Button button = event.getButton();
        final int index = buttonGroup.indexOfButton(button);
        if (!patternIndex.equals(index)) {
            patternIndex = index;
            design.getCreateButton().removeStyleName(StyleNames.BUTTON_ACTIVE);
            button.addStyleName(StyleNames.BUTTON_ACTIVE);
            design.getSubmitButton().setCaption(I18N.getString(PTRN_MESSAGE_SAVE));
            pattern = BrownieSession.getMasterdata().getPolls().get(patternIndex);
            design.getBodyField().setValue(pattern.getBody());
            design.getChoiceSelect().setStrings(pattern.getAnswers().stream().filter(answer -> !answer.isCustom())
                    .map(PollAnswer::getSequence).collect(Collectors.toList()));
            final PollAnswer customAnswer = pattern.findCustomAnswer();
            design.getAllowCustomField().setValue(customAnswer != null);
            if (customAnswer == null) {
                design.getCustomField().clear();
            } else {
                design.getCustomField().setValue(customAnswer.getSequence());
            }
            design.getAllowMultiplyField().setValue(pattern.isMultiple());
            design.getTagSelect().setStrings(pattern.getTags());
            folder = pattern.getFolder();
            design.getCurrentFolderLabel().setValue(folder.getTitle());
        }*/
    }

    private void clickCreateButton() {
        design.getCreateButton().addStyleName(StyleNames.BUTTON_ACTIVE);
        if (buttonGroup.getSelectedButton() != null) {
            buttonGroup.getSelectedButton().removeStyleName(StyleNames.BUTTON_ACTIVE);
        }
        design.getSubmitButton().setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_ADD));
        if (patternIndex >= 0) {
            patternIndex = -1;
            clear();
        }
    }

    private void clickSubmitButton() {
        /*if (!validate()) {
            return;
        }

        final PatternService service = new PatternService(BrownieSession.get(), BrownieConfiguration.get());
        if (patternIndex >= 0) {
            // edit existed pattern
            PollPatternRequest model = new PollPatternRequest();
            model.setSid(pattern.getSid());
            model.setDeleted(Pattern.DELETED);
            model.setBody(design.getBodyField().getValue().trim());
            model.setChoiceType(design.getAllowMultiplyField().getValue() ? PollQuestionType.MULTIPLE_ANSWERS.toString()
                    : PollQuestionType.SINGLE_ANSWER.toString());
            model.setAnswers(design.getChoiceSelect().getStrings());

            model.setCustom(design.getAllowCustomField().getValue());
            if (design.getAllowCustomField().getValue()) {
                model.setCustomAnswer(design.getCustomField().getValue().trim());
            }
            model.setFolderId(folder.getId());
            model.setTags(design.getTagSelect().getUniqueStrings());

            PollPatternsRequest request = new PollPatternsRequest(Arrays.asList(model));
            PollPatternsResponse response;
            try {
                response = service.setPolls(request);
            } catch (RemoteServiceException e) {
                BrownieErrorHandler.handle(e, I18N.getString(PTRN_POLL_EDIT_ERROR));
                return;
            }
            PollPattern pattern = PollPattern.valueOf(response.getData().get(0));
            List<PollPattern> patterns = BrownieSession.getMasterdata().getPolls();
            patterns.set(patternIndex, pattern);
            Collections.sort(patterns);
            int index = patterns.stream().map(Pattern::getSid).collect(Collectors.toList()).indexOf(pattern.getSid());
            patternIndex = (index < 0) ? 0 : index;
            Tray.show(I18N.getString(PTRN_POLL_UPDATED));
            update();
        } else {
            // create new pattern
            PollPatternRequest model = new PollPatternRequest();
            model.setBody(design.getBodyField().getValue().trim());
            model.setChoiceType(design.getAllowMultiplyField().getValue() ? PollQuestionType.MULTIPLE_ANSWERS.toString()
                    : PollQuestionType.SINGLE_ANSWER.toString());
            model.setAnswers(design.getChoiceSelect().getStrings());
            model.setCustom(design.getAllowCustomField().getValue());
            if (design.getAllowCustomField().getValue()) {
                model.setCustomAnswer(design.getCustomField().getValue().trim());
            }
            model.setFolderId(folder.getId());
            model.setTags(design.getTagSelect().getUniqueStrings());
            model.setDeleted(false);

            PollPatternsRequest request = new PollPatternsRequest(Arrays.asList(model));
            PollPatternsResponse response = null;
            try {
                response = service.setPolls(request);
            } catch (RemoteServiceException e) {
                BrownieErrorHandler.handle(e, I18N.getString(PTRN_POLL_CREATE_ERROR));
            }
            if (response != null) {
                PollPattern pattern = PollPattern.valueOf(response.getData().get(0));
                List<PollPattern> patterns = BrownieSession.getMasterdata().getPolls();
                patterns.add(pattern);
                Collections.sort(patterns);
                Tray.show(I18N.getString(PTRN_POLL_CREATED));
                clear();
                update();
            }
        }*/
    }
}
