package ru.klapatnyuk.sberbank.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.toggle.ButtonGroupSelectionEvent;

import javax.swing.*;

/**
 * @author klapatnyuk
 */
public class DocumentTab extends AbstractTab {

    private static final long serialVersionUID = 621247325187983282L;
    private static final Logger LOG = LoggerFactory.getLogger(DocumentTab.class);
    private static final int LABEL_LENGTH = Integer.parseInt(SberbankUI.CONFIG.getString(ConfigKey.MESSAGE_SUBJECT_LENGTH.getKey()));

    //private final PollService service = new PollService(BrownieSession.get(), BrownieConfiguration.get());
    //private final Map<Long, PollResponse> pollModels = new TreeMap<>(Collections.reverseOrder());

    private Long pollId;

    private ButtonGroup pollButtonGroup = new ButtonGroup();
    private DocumentTabView design;

    public DocumentTab(MenuTab tab, MenuTab actionTab) {
        super(tab, actionTab);
    }

    @Override
    public boolean isUpdated() {
        return false;
    }

    @Override
    public void update() {
        /*PollsResponse response;
        try {
            response = service.getPolls(nts.trueip.frontend.common.rpc.brownie.poll.PollService.PollBusinessStatus.ALL);
        } catch (RemoteServiceException e) {
            BrownieErrorHandler.handle(e, I18N.getString(MSGR_POLLS_RECEIVE_ERROR));
            return;
        }
        pollButtonGroup = new ButtonGroup();
        design.getPollLayout().removeAllComponents();
        pollModels.clear();

        final List<String> captions = new ArrayList<>();
        response.getPolls().sort(new PollComparator());
        response.getPolls().forEach(poll -> {
            captions.add(poll.getBody());
            pollModels.put(poll.getId(), poll);
        });
        for (String caption : captions) {
            final String croppedCaption = (caption.length() > LABEL_LENGTH) ? caption.substring(0, LABEL_LENGTH) + ".." : caption;
            Button button = new Button(croppedCaption);
            button.setDescription(caption);
            pollButtonGroup.addButton(button);
        }
        final Button[] buttons = pollButtonGroup.getButtons();
        pollButtonGroup.addSelectionListener(event -> selectPoll(event));
        Arrays.asList(buttons).forEach(button -> design.getPollLayout().addComponent(button));
        if (buttons.length > 0) {
            design.getPollLayout().removeComponent(design.getEmptyLabel());
            int index = 0;
            if (pollId != null && pollModels.containsKey(pollId)) {
                index = new ArrayList<>(pollModels.keySet()).indexOf(pollId);
            }
            pollButtonGroup.setSelectedButtonIndex(index);
            selectPoll(new ButtonGroupSelectionEvent(pollButtonGroup, buttons[index], null));
        } else {
            design.getPollLayout().addComponent(design.getEmptyLabel());
        }

        boolean empty = buttons.length == 0;
        design.getEmptySelectionLabel().setVisible(empty);
        design.getStatusLabel().setVisible(!empty);
        design.getQuestionLabel().setVisible(!empty);
        design.getChoiceLayout().setVisible(!empty);
        design.getPrintLayout().setVisible(!empty);*/
    }

    @Override
    public String getHeader() {
        return SberbankUI.I18N.getString(SberbankKey.Header.H2, SberbankUI.I18N.getString(SberbankKey.Menu.MSGR),
                SberbankUI.I18N.getString(SberbankKey.Header.MSGR_POLLS));
    }

    @Override
    protected TabView getDesign() {
        if (design == null) {
            design = new DocumentTabView();
        }
        return design;
    }

    private void selectPoll(ButtonGroupSelectionEvent event) {
        /*Button previousButton = event.getPreviousButton();
        if (previousButton != null) {
            pollButtonGroup.getButton(pollButtonGroup.indexOfButton(previousButton)).removeStyleName("active");
        }
        Button selectedButton = event.getSelectedButton();
        pollButtonGroup.getButton(pollButtonGroup.indexOfButton(selectedButton)).addStyleName("active");
        pollId = new ArrayList<>(pollModels.keySet()).get(pollButtonGroup.indexOfButton(selectedButton));
        LOG.debug("Current poll id: " + pollId);
        updatePollPanel();*/
    }

    private void updatePollPanel() {
        /*LOG.debug("Poll panel update started");

        PollResponse currentPollModel = pollModels.get(pollId);
        PollsResponse response;
        try {
            response = service.getPollById(currentPollModel.getId(), true);
        } catch (RemoteServiceException e) {
            BrownieErrorHandler.handle(e, I18N.getString(MSGR_POLLS_RECEIVE_SPECIFIC_ERROR));
            return;
        }
        PollResponse model = response.getPolls().get(0);
        design.getStatusLabel().setStyleName("status-label");

        if (model.getPollStatus() == PollStatus.IN_PROGRESS) {
            design.getStatusLabel().addStyleName(StyleNames.LABEL_ACTIVE);
            design.getStatusLabel().setValue(I18N.getString(MSGR_POLLS_STATUS_ACTIVE));
        } else if (model.getPollStatus() == PollStatus.FINISHED) {
            design.getStatusLabel().addStyleName(StyleNames.LABEL_FINISHED);
            design.getStatusLabel().setValue(I18N.getString(MSGR_POLLS_STATUS_FINISHED));
        } else {
            design.getStatusLabel().addStyleName(StyleNames.LABEL_STOPPED);
            design.getStatusLabel().setValue(I18N.getString(MSGR_POLLS_STATUS_STOPPED));
        }

        design.getQuestionLabel().setValue(model.getBody().replaceAll("(\r\n|\n)", "<br />").replaceAll("(<br />){2,}", "<br />"));

        design.getChoiceLayout().removeAllComponents();
        QuestionResponse question = model.getQuestion();
        List<ChoiceResponse> choices = question.getChoices();

        // calculate percents
        double max = 0;
        for (ChoiceResponse choice : choices) {
            if (choice.getQuantity() == null) {
                AbstractUI.getWarningWindow().add(I18N.getString(MSGR_POLLS_RECEIVE_RESULT_ERROR));
            } else if (choice.getQuantity() > max) {
                max = choice.getQuantity();
            }
        }
        for (ChoiceResponse choice : choices) {
            Label choiceLabel = new Label(I18N.getString(MSGR_POLLS_CHOICE, choice.getValue()));
            choiceLabel.setHeight(StyleDimensions.HEIGHT_S);
            Component percentComponent;
            if (choice.getQuantity() == 0) {
                Label percentLabel = new Label(choice.getQuantity().toString());
                percentLabel.setHeight(StyleDimensions.HEIGHT_S);
                percentLabel.setWidthUndefined();
                percentLabel.setStyleName("label-percent");
                percentComponent = percentLabel;
            } else {
                Button percentButton = new Button(choice.getQuantity().toString());
                percentButton.setHeight(StyleDimensions.HEIGHT_S);
                percentButton.setWidth(Math.round(choice.getQuantity() / max * 100) + "%");
                percentComponent = percentButton;
            }
            VerticalLayout choiceLayout = new VerticalLayout();
            choiceLayout.setStyleName("choice-layout");
            choiceLayout.addComponent(choiceLabel);
            choiceLayout.addComponent(percentComponent);
            design.getChoiceLayout().addComponent(choiceLayout);
        }*/
    }
}