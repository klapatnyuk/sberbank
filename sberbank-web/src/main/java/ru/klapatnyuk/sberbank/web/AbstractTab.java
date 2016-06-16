package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.HorizontalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klapatnyuk
 */
public abstract class AbstractTab extends HorizontalLayout implements Tab {

    private static final long serialVersionUID = -6229564821654218076L;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractTab.class);

    protected MenuTab tab;
    protected MenuTab actionTab;

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
        LOG.debug("Tab's update started");
    }

    @Override
    public void poll() {
        LOG.debug("Tab's poll started");
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible && !isUpdated()) {
            update();
        }
        super.setVisible(visible);
    }

    protected void init() {
        LOG.debug("Tab's init started");
        setSizeFull();
        addComponent(getDesign());
    }

    protected abstract TabView getDesign();
}