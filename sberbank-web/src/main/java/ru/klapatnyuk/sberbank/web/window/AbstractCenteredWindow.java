package ru.klapatnyuk.sberbank.web.window;

import com.vaadin.server.Page;
import com.vaadin.ui.Window;

import java.util.ResourceBundle;

/**
 * @author klapatnyuk
 */
abstract public class AbstractCenteredWindow extends Window {

    private static final long serialVersionUID = 7861330826953738391L;

    protected final ResourceBundle bundle;

    private Page.BrowserWindowResizeListener resizeListener;


    public AbstractCenteredWindow(ResourceBundle bundle, String caption) {
        super(caption);
        this.bundle = bundle;
        init();
    }

    protected void init() {
        resizeListener = new WindowResizeListener();

        setClosable(false);
        setDraggable(false);
        setResizable(false);

        addAttachListener(new WindowAttachListener());
        addDetachListener(new WindowDetachListener());

        Page page = Page.getCurrent();
        if (page != null) {
            Page.getCurrent().addBrowserWindowResizeListener(resizeListener);
        }

        initContent();
    }

    protected abstract void initContent();

    /**
     * @author klapatnyuk
     */
    private class WindowAttachListener implements AttachListener {

        private static final long serialVersionUID = 3419954352383314183L;

        @Override
        public void attach(AttachEvent event) {
            center();
            Page.getCurrent().addBrowserWindowResizeListener(resizeListener);
        }
    }

    /**
     * @author klapatnyuk
     */
    private class WindowDetachListener implements DetachListener {

        private static final long serialVersionUID = -2364132134576825582L;

        @Override
        public void detach(DetachEvent event) {
            Page.getCurrent().removeBrowserWindowResizeListener(resizeListener);
        }
    }

    /**
     * @author klapatnyuk
     */
    private class WindowResizeListener implements Page.BrowserWindowResizeListener {

        private static final long serialVersionUID = -3775176518756366967L;

        @Override
        public void browserWindowResized(Page.BrowserWindowResizeEvent event) {
            center();
        }
    }
}