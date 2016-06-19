package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.toggle.ButtonGroupSelectionEvent;
import ru.klapatnyuk.sberbank.model.entity.Document;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.handler.DocumentHandler;
import ru.klapatnyuk.sberbank.model.handler.FieldHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author klapatnyuk
 */
public class DocumentTab extends AbstractTab<Document> {

    private static final long serialVersionUID = 5490116135419202151L;
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentTab.class);

    private DocumentTabView design;

    public DocumentTab(MenuTab tab, MenuTab actionTab) {
        super(tab, actionTab);
    }

    @Override
    public void update() {
        super.update();

        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            entities = new DocumentHandler(connection).findAll();
            SberbankUI.connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Templates finding error", e);
            // TODO display WarningMessage
        }

        updateEntityLayout();
    }

    @Override
    public String getHeader() {
        return SberbankUI.I18N.getString(SberbankKey.Header.H2, SberbankUI.I18N.getString(SberbankKey.Menu.MSGR),
                SberbankUI.I18N.getString(SberbankKey.Header.PTRN_POLL));
    }

    @Override
    public boolean validate() {
        // validate title
        List<WarningMessage> messages = validateTitle();

        SberbankUI.getWarningWindow().addAll(messages);
        return messages.isEmpty();
    }

    @Override
    public void clear() {
        design.getTemplateSelect().clear();

//        design.getChoiceSelect().clear();
//        design.getAllowCustomField().clear();
//        design.getCustomField().clear();
//        design.getAllowMultiplyField().clear();
//        design.getTagSelect().clear();
    }

    @Override
    protected void init() {
        super.init();

        design.getCreateButton().addClickListener(event -> clickCreateButton());
        design.getSubmitButton().addClickListener(event -> clickSubmitButton());
    }

    @Override
    protected AbstractTabView getDesign() {
        if (design == null) {
            design = new DocumentTabView();
        }
        return design;
    }

    @Override
    protected void selectEntity(ButtonGroupSelectionEvent event) {
        super.selectEntity(event);

        design.getTemplateSelect().removeAllItems();
        design.getTemplateSelect().addItem(entity.getTemplate().getTitle());
        design.getTemplateSelect().setValue(entity.getTemplate().getTitle());
        design.getTemplateSelect().setEnabled(false);

        design.getTitleField().setValue(entity.getTitle());

        List<Field> fields = null;
        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            fields = new FieldHandler(connection).findByDocumentId(entities.get(entityIndex).getId());
            SberbankUI.connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Templates finding error", e);
            // TODO display WarningMessage
        }

        if (fields == null) {
            return;
        }
        //design.getTemplateLayout().setFields(fields);
    }

    @Override
    protected void clickEntityButton(Button.ClickEvent event) {
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

    @Override
    protected void clickSubmitButton() {
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
