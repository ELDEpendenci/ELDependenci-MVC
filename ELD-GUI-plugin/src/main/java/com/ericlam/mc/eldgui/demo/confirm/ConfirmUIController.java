package com.ericlam.mc.eldgui.demo.confirm;

import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.controller.ControllerForView;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.event.BaseHandler;
import com.ericlam.mc.eldgui.event.ClickHandler;

@ControllerForView(ConfirmUIView.class)
public class ConfirmUIController implements UIController {

    @ClickHandler(base = @BaseHandler(patterns = {'A'}))
    public String onConfirm(UISession session) {
        session.setAttribute("confirm", true);
        return "crafttable";
    }


    @ClickHandler(base = @BaseHandler(patterns = {'B'}))
    public String onCancel(UISession session) {
        session.setAttribute("confirm", false);
        return "crafttable";
    }
}
