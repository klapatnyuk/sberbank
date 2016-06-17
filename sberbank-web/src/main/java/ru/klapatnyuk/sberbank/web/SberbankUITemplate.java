package ru.klapatnyuk.sberbank.web;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.declarative.Design;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author klapatnyuk
 */
@DesignRoot
public class SberbankUITemplate extends VerticalLayout implements UITemplate {

    private static final long serialVersionUID = 4433227540082907242L;
    private static final Logger LOG = LoggerFactory.getLogger(SberbankUITemplate.class);

    private final Map<MenuTab, Map<MenuTab, AbstractTab>> tabs = new TreeMap<>();
    private final MenuBar.Command actionCommand = new ActionMenuCommand();
    private final MenuCommand menuCommand = new MenuCommand();

    private MenuTab tab = SberbankMenuTab.EDITOR;
    private MenuTab actionTab;

    @SuppressWarnings("unused")
    private Label headerLabel;
    @SuppressWarnings("unused")
    private HorizontalLayout headerLayout;
    @SuppressWarnings("unused")
    private HorizontalLayout tabsLayout;
    @SuppressWarnings("unused")
    private ProfileLayout profileLayout;
    @SuppressWarnings("unused")
    private HorizontalLayout filterLayout;
    @SuppressWarnings("unused")
    private Label copyrightLabel;
    @SuppressWarnings("unused")
    private MenuBar menuBar;
    @SuppressWarnings("unused")
    private MenuBar actionMenuBar;
    @SuppressWarnings("unused")
    private Image logoImage;
    @SuppressWarnings("unused")
    private HorizontalLayout topSection;
    @SuppressWarnings("unused")
    private HorizontalLayout headerSection;
    @SuppressWarnings("unused")
    private HorizontalLayout footerSection;

    public SberbankUITemplate() {
        Design.read(this);

        initTabs();
        initTopSection();
        initHeaderSection();
        initMainSection();
        initFooterSection();

        clickMenu(tab);
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

    private void initProfileBar() {
        profileLayout.getLogoutButton().addClickListener(event -> SberbankUI.getCurrent().login());

        profileLayout.getLogoutButton().addClickListener(event -> {
            // TODO add LoginService invocation
        });
    }

    private void initCopyright() {
        String contacts = SberbankUI.I18N.getString(SberbankKey.Header.FOOTER_COPYRIGHT,
                ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy")));
        String copyright = SberbankUI.I18N.getString(SberbankKey.Header.FOOTER_PRODUCTION, contacts);
        copyrightLabel.setValue(copyright);
    }

    private void initTabs() {

        AbstractTab document = new DocumentTab(SberbankMenuTab.EDITOR, EditorMenuTab.DOCUMENT);
        AbstractTab template = new TemplateTab(SberbankMenuTab.EDITOR, EditorMenuTab.TEMPLATE);

        Map<MenuTab, AbstractTab> editorTabs = new TreeMap<>();
        editorTabs.put(EditorMenuTab.DOCUMENT, document);
        editorTabs.put(EditorMenuTab.TEMPLATE, template);

        tabs.put(SberbankMenuTab.EDITOR, editorTabs);
    }

    private void initMenuBar() {
        menuBar.addItem(SberbankUI.I18N.getString(SberbankKey.Menu.MSGR), menuCommand);
    }

    private void initFilterBar() {
        Button button = new Button(SberbankUI.I18N.getString(SberbankKey.Form.FILTER_LIST));
        button.setWidth(StyleDimensions.WIDTH_S);
        button.setHeight(StyleDimensions.HEIGHT_S);
        button.addClickListener(this::clickFilterButton);
        filterLayout.addComponent(button);
    }

    private void reloadActionMenuBar() {
        actionMenuBar.removeItems();
        actionMenuBar.addItem(SberbankUI.I18N.getString(SberbankKey.Menu.MSGR_IN), actionCommand);
        actionMenuBar.addItem(SberbankUI.I18N.getString(SberbankKey.Menu.MSGR_OUT), actionCommand);
    }

    private void clickFilterButton(Button.ClickEvent event) {

        /*if (getTab() instanceof ReviewTab) {
            ReviewTab layout = (ReviewTab) getTab();
            boolean filter = layout.isFilter();
            filter = !filter;

            layout.setFilter(filter);
            layout.getTreePanel().setVisible(filter);
            layout.getSubmitPanel().setVisible(filter && !StringUtils.isEmpty(layout.getRecipient()));
            layout.getVerticalSeparator().setVisible(filter);

            if (filter) {
                event.getButton().setCaption(AbstractUI.I18N.getString(FILTER_LIST));
            } else {
                event.getButton().setCaption(AbstractUI.I18N.getString(FILTER_GROUP));
            }

            headerLabel.setValue(layout.getHeader());

            SimpleMonologComponent dialog = layout.getDialog();
            dialog.clear();
            if (filter) {
                layout.clear();
            } else {
                layout.setRecipient(RequestConstant.FROM_ALL);
                dialog.setRecipient(RequestConstant.FROM_ALL);
                layout.getDialogContainer().setContent(dialog);
                layout.update();
            }
            layout.setFilter(filter);

        } else if (getTab() instanceof RequestsTab) {
            RequestsTab layout = (RequestsTab) getTab();
            boolean filter = layout.isFilter();
            filter = !filter;

            layout.setFilter(filter);
            layout.getTreePanel().setVisible(filter);
            layout.getVerticalSeparator().setVisible(filter);

            if (filter) {
                event.getButton().setCaption(AbstractUI.I18N.getString(FILTER_LIST));
            } else {
                event.getButton().setCaption(AbstractUI.I18N.getString(FILTER_GROUP));
            }
            headerLabel.setValue(layout.getHeader());

            TaskListedComponent dialog = layout.getDialog();
            dialog.clear();
            if (filter) {
                layout.clear();
            } else {
                layout.setRecipient(RequestConstant.FROM_ALL);
                dialog.setRecipient(RequestConstant.FROM_ALL);
                layout.getDialogContainer().setContent(dialog);
                layout.update();
            }
            layout.setFilter(filter);
        }*/
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

    /**
     * @author klapatnyuk
     */
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
        }
    }

    /**
     * @author klapatnyuk
     */
    private class ActionMenuCommand implements MenuBar.Command {

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
}