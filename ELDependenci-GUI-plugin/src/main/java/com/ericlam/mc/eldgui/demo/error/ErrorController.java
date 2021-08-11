package com.ericlam.mc.eldgui.demo.error;

import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.event.ClickMapping;
import com.ericlam.mc.eldgui.view.BukkitRedirectView;
import com.ericlam.mc.eldgui.view.BukkitView;

@UIController("error")
public class ErrorController {

    public BukkitView<?, ?> index(UISession session){
        Exception ex = session.pollAttribute("ex");
        return new BukkitView<>(ErrorView.class, ex);
    }

    @ClickMapping(view = ErrorView.class, pattern = 'B')
    public BukkitView<?,?> clickBack(UISession session){
        String fallback = session.pollAttribute("from");
        return new BukkitRedirectView(fallback);
    }
}
