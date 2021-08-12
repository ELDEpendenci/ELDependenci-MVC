package com.ericlam.mc.eldgui.demo.error;

import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.event.ClickMapping;
import com.ericlam.mc.eldgui.view.BukkitRedirectView;
import com.ericlam.mc.eldgui.view.BukkitView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UIController("error")
public class ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    public BukkitView<?, ?> index(UISession session) {
        Exception ex = session.pollAttribute("exception");
        if (ex == null) {
            LOGGER.warn("exception is null, going to test error gui");
            return new BukkitView<>(TestErrorView.class, null);
        }
        return new BukkitView<>(ErrorView.class, ex);
    }

    @ClickMapping(view = ErrorView.class, pattern = 'B')
    public BukkitView<?, ?> clickBack(UISession session) {
        String fallback = session.pollAttribute("from");
        if (fallback == null) {
            LOGGER.warn("fallback target is null, using test error gui");
            return new BukkitView<>(TestErrorView.class, null);
        }
        return new BukkitRedirectView(fallback);
    }


    @ClickMapping(view = TestErrorView.class, pattern = 'A')
    public BukkitView<?, ?> onTestError(UISession session) {
        session.setAttribute("exception", new IllegalStateException("THIS IS A TEST ERROR"));
        return index(session);
    }
}
