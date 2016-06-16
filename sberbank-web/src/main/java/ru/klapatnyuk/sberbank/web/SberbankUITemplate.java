package ru.klapatnyuk.sberbank.web;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.declarative.Design;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author klapatnyuk
 */
public class SberbankUITemplate extends VerticalLayout implements UITemplate {

    protected final Map<MenuTab, Map<MenuTab, AbstractTab>> tabs;
    protected final MenuCommand menuCommand;
    protected final MenuBar.Command actionCommand;

    protected MenuTab tab;
    protected MenuTab actionTab;
    protected Map<Integer, Resource> icons;

    protected Label headerLabel;
    protected Label copyrightLabel;
    protected HorizontalLayout headerLayout;
    protected HorizontalLayout tabsLayout;
    protected ProfileLayout profileLayout;
    protected HorizontalLayout filterLayout;
    protected MenuBar menuBar;
    protected MenuBar actionMenuBar;

    private static final long serialVersionUID = 4433227540082907242L;
    private static final Logger LOG = LoggerFactory.getLogger(SberbankUITemplate.class);

    private Image logoImage;
    private HorizontalLayout topSection;
    private HorizontalLayout headerSection;
    private HorizontalLayout footerSection;

    public SberbankUITemplate(MenuTab defaultTab) {
        tabs = new TreeMap<>();
        menuCommand = new MenuCommand();
        actionCommand = new ActionMenuCommand();
        tab = defaultTab;

        Design.read(this);
        init();
        clickMenu(tab);
    }

    public Map<Integer, Resource> getIcons() {
        return icons;
    }

    @Override
    public ProfileLayout getProfileLayout() {
        return profileLayout;
    }

    @Override
    public HorizontalLayout getFilterLayout() {
        return filterLayout;
    }

    @Override
    public MenuBar getActionMenuBar() {
        return actionMenuBar;
    }

    @Override
    public void setHeader(String header) {
        headerLabel.setValue(header);
    }

    @Override
    public void clickMenu(MenuTab tab) {
        MenuBar.MenuItem item = menuBar.getItems().get(tab.getIndex());
        item.getCommand().menuSelected(item);
    }

    @Override
    public void clickActionMenu(MenuTab tab) {
        MenuBar.MenuItem item = actionMenuBar.getItems().get(tab.getIndex());
        item.getCommand().menuSelected(item);
    }

    @Override
    public void poll() {
        SberbankUI.getWarningWindow().poll();
        getTab().poll();
    }

    @Override
    public AbstractTab getTab() {
        return tabs.get(tab).get(actionTab);
    }

    @Override
    public AbstractTab getTab(MenuTab actionTab) {
        if (tabs.get(tab).containsKey(actionTab)) {
            return tabs.get(tab).get(actionTab);
        }
        return null;
    }

    @Override
    public AbstractTab getTab(MenuTab tab, MenuTab actionTab) {
        if (tabs.containsKey(tab) && tabs.get(tab).containsKey(actionTab)) {
            return tabs.get(tab).get(actionTab);
        }
        return null;
    }

    protected void init() {
        icons = new HashMap<>();
        icons.put(0, new ThemeResource("img/counter/plus-16x16.png"));
        for (int key = 1; key < 10; key++) {
            icons.put(key, new ThemeResource("img/counter/" + key + "-16x16.png"));
        }

        initTabs();
        initTopSection();
        initHeaderSection();
        initMainSection();
        initFooterSection();
    }

    protected void initProfileBar() {
        profileLayout.getLogoutButton().addClickListener(event -> SberbankUI.getCurrent().login());
    }

    protected void initCopyright() {
        String contacts = SberbankUI.I18N.getString(SberbankKey.Header.FOOTER_COPYRIGHT,
                ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy")));
        String copyright = SberbankUI.I18N.getString(SberbankKey.Header.FOOTER_PRODUCTION, contacts);
        copyrightLabel.setValue(copyright);
    }

    protected void initTabs() {

        /*// init location tab layouts

        final AbstractTab review = new ReviewTab(ConciergeMenuTab.LOCATION, LocationMenuTab.REVIEW);
        final AbstractTab advert = new AdvertTab(ConciergeMenuTab.LOCATION, LocationMenuTab.ADVERT);
        final AbstractTab polls = new PollsTab(ConciergeMenuTab.LOCATION, LocationMenuTab.POLLS);
        final AbstractTab poll = new PollTab(ConciergeMenuTab.LOCATION, LocationMenuTab.POLL);
        final AbstractTab requests = new RequestsTab(ConciergeMenuTab.LOCATION, LocationMenuTab.REQUESTS);
        final AbstractTab history = new HistoryTab(ConciergeMenuTab.LOCATION, LocationMenuTab.HISTORY);

        // init pattern tab layouts

        final AbstractTab message = new MessagePatternTab(ConciergeMenuTab.PATTERN, PatternMenuTab.MESSAGE);
        final AbstractTab pollPattern = new PollPatternTab(ConciergeMenuTab.PATTERN, PatternMenuTab.POLL);
        final AbstractTab style = new StyleTab(ConciergeMenuTab.PATTERN, PatternMenuTab.STYLE);

        // init all action tab maps

        final Map<MenuTab, AbstractTab> locationTabs = new TreeMap<>();
        locationTabs.put(LocationMenuTab.REVIEW, review);
        locationTabs.put(LocationMenuTab.ADVERT, advert);
        locationTabs.put(LocationMenuTab.POLLS, polls);
        locationTabs.put(LocationMenuTab.POLL, poll);
        locationTabs.put(LocationMenuTab.REQUESTS, requests);
        locationTabs.put(LocationMenuTab.HISTORY, history);

        final Map<MenuTab, AbstractTab> patternTabs = new TreeMap<>();
        patternTabs.put(PatternMenuTab.MESSAGE, message);
        patternTabs.put(PatternMenuTab.POLL, pollPattern);
        patternTabs.put(PatternMenuTab.STYLE, style);

        // init tab map

        tabs.put(ConciergeMenuTab.LOCATION, locationTabs);
        tabs.put(ConciergeMenuTab.PATTERN, patternTabs);

        // init poll action menu commends

        pollMenuCommands.put(ConciergeMenuTab.LOCATION, new LocationPollCommand());
        pollMenuCommands.put(ConciergeMenuTab.PATTERN, new ClearPollCommand());*/
    }

    protected void initMenuBar() {
        String addressDisplayName = BrownieSession.get().getAddressDisplayName();
        if (addressDisplayName == null || addressDisplayName.isEmpty()) {
            menuBar.addItem(SberbankUI.I18N.getString(MSGR), menuCommand);
        } else {
            menuBar.addItem(addressDisplayName, menuCommand);
        }
        menuBar.addItem(SberbankUI.I18N.getString(PTRN), menuCommand);
    }

    protected void initFilterBar() {
        Button button = new Button(SberbankUI.I18N.getString(FILTER_LIST));
        button.setWidth(StyleDimensions.WIDTH_S);
        button.setHeight(StyleDimensions.HEIGHT_S);
        button.addClickListener(this::clickFilterButton);
        filterLayout.addComponent(button);
    }

    protected void reloadActionMenuBar() {
        actionMenuBar.removeItems();
        if (tab == ConciergeMenuTab.LOCATION) {
            actionMenuBar.addItem(SberbankUI.I18N.getString(MSGR_IN), actionCommand);
            actionMenuBar.addItem(SberbankUI.I18N.getString(MSGR_OUT), actionCommand);
            actionMenuBar.addItem(SberbankUI.I18N.getString(MSGR_POLLS), actionCommand);
            actionMenuBar.addItem(SberbankUI.I18N.getString(MSGR_POLL), actionCommand);
            actionMenuBar.addItem(SberbankUI.I18N.getString(MSGR_REQUESTS), actionCommand);
            actionMenuBar.addItem(SberbankUI.I18N.getString(MSGR_HISTORY), actionCommand);

        } else if (tab == ConciergeMenuTab.PATTERN) {
            actionMenuBar.addItem(SberbankUI.I18N.getString(PTRN_MESSAGE), actionCommand);
            actionMenuBar.addItem(SberbankUI.I18N.getString(PTRN_POLL), actionCommand);
            actionMenuBar.addItem(SberbankUI.I18N.getString(PTRN_STYLE), actionCommand);
        }
    }

    private void initTopSection() {
        topSection.setHeight("36px");
        logoImage.setSource(new ThemeResource("img/logo.png"));
        initProfileBar();
    }

    private void initHeaderSection() {
        headerSection.setHeightUndefined();
        menuBar.setHeight("44px");
        initMenuBar();
        actionMenuBar.setHeight("50px");
        headerLayout.setHeight("36px");
        initFilterBar();
    }

    private void initMainSection() {
        initTabsLayout();
    }

    private void initTabsLayout() {
        tabs.forEach((tab, layouts) -> layouts.forEach((subTab, layout) -> tabsLayout.addComponent(layout)));
    }

    private void initFooterSection() {
        footerSection.setHeight("36px");
        initCopyright();
        copyrightLabel.setWidthUndefined();
    }

    private void updateMenuBar(int old) {
        menuBar.getItems().get(old).setStyleName(StyleNames.MENU_ITEM_INACTIVE);
        menuBar.getItems().get(tab.getIndex()).setStyleName(StyleNames.MENU_ITEM_ACTIVE);
    }

    // TODO needs to be refactored
    private void updateTabsLayout(MenuTab old) {
        tabs.forEach((tab, layouts) -> layouts.forEach((subTab, layout) -> layout.setVisible(false)));
        getTab().setVisible(true);
    }

    private void updateActionMenuBar(int old) {
        actionMenuBar.getItems().get(old).setStyleName(StyleNames.MENU_ITEM_INACTIVE);
        actionMenuBar.getItems().get(actionTab.getIndex()).setStyleName(StyleNames.MENU_ITEM_ACTIVE);
    }

    protected class MenuCommand implements MenuBar.Command {

        private static final long serialVersionUID = -5336826706220710041L;

        public MenuCommand() {}

        @Override
        public void menuSelected(MenuBar.MenuItem item) {
            LOG.debug("Selected main menu item: " + item.getText());

            // save old state
            int oldIndex = tab.getIndex();
            MenuTab oldActionTab = actionTab;
            MenuTab oldTab = tab;

            // update model
            tab = tab.get(menuBar.getItems().indexOf(item));
            actionTab = tab.getDefaultSub();

            // update view
            updateMenuBar(oldIndex);
            reloadActionMenuBar();
            if (oldTab != null && oldActionTab != null) {
                tabs.get(oldTab).get(oldActionTab).setVisible(false);
            }

            clickActionMenu(actionTab);
            pollMenuCommands.get(tab).poll();
        }
    }

    protected class ActionMenuCommand implements MenuBar.Command {

        private static final long serialVersionUID = 5134839118306122334L;

        public ActionMenuCommand() {}

        @Override
        public void menuSelected(MenuBar.MenuItem item) {
            LOG.debug("Selected action menu item: " + item.getText());

            // save old sate
            final int oldIndex = actionTab.getIndex();
            final MenuTab oldActionTab = actionTab;

            // update model
            actionTab = actionTab.get(actionMenuBar.getItems().indexOf(item));

            headerLabel.setValue(getTab(tab, actionTab).getHeader());
            updateActionMenuBar(oldIndex);
            updateTabsLayout(oldActionTab);
        }
    }

    /**
     * @author vykla
     */
    protected class ClearPollCommand implements PollMenuCommand {

        public ClearPollCommand() {}

        @Override
        public void poll() {
            LOG.debug("Clear poll command started");
            actionMenuBar.getItems().forEach(item -> item.setIcon(null));
        }
    }
}
