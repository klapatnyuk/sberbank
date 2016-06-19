package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.toggle.ButtonGroup;
import org.vaadin.addons.toggle.ButtonGroupSelectionEvent;
import ru.klapatnyuk.sberbank.model.entity.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author klapatnyuk
 */
public abstract class AbstractTab<T extends AbstractEntity> extends HorizontalLayout implements Tab {

    protected static final int LENGTH =
            Integer.parseInt(SberbankUI.CONFIG.getString(ConfigKey.PATTERN_SUBJECT_LENGTH.getKey()));

    protected List<T> entities = new ArrayList<>();
    protected MenuTab tab;
    protected MenuTab actionTab;
    protected int entityIndex = -1;
    protected ButtonGroup buttonGroup = new ButtonGroup();
    protected T entity;

    private static final long serialVersionUID = -6229564821654218076L;
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTab.class);

    public AbstractTab(MenuTab tab, MenuTab actionTab) {
        this.tab = tab;
        this.actionTab = actionTab;

        init();
        setVisible(false);
    }

    @Override
    public MenuTab getTab() {
        return tab;
    }

    @Override
    public MenuTab getActionTab() {
        return actionTab;
    }

    @Override
    public void update() {
        LOGGER.debug("Tab's update started");
    }

    @Override
    public void poll() {
        LOGGER.debug("Tab's poll started");
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible && !isUpdated()) {
            update();
        }
        super.setVisible(visible);
    }

    @Override
    public boolean isUpdated() {
        return false;
    }

    @Override
    public Component getValidationSource() {
        return getDesign().getSubmitButton();
    }

    protected void init() {
        LOGGER.debug("Tab's init started");
        setSizeFull();
        addComponent(getDesign());
    }

    protected void clickCreateButton() {
        if (entityIndex < 0) {
            return;
        }

        // update model
        entityIndex = -1;

        // update styles
        getDesign().getCreateButton().addStyleName(StyleNames.BUTTON_ACTIVE);
        if (buttonGroup.getSelectedButton() != null) {
            buttonGroup.getSelectedButton().removeStyleName(StyleNames.BUTTON_ACTIVE);
        }

        // update form
        clear();
        getDesign().getSubmitButton().setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_ADD));
    }

    protected abstract TabView getDesign();

    protected abstract void selectEntity(ButtonGroupSelectionEvent event);

    protected abstract void clickEntityButton(Button.ClickEvent event);

    protected abstract void clickSubmitButton();
}
