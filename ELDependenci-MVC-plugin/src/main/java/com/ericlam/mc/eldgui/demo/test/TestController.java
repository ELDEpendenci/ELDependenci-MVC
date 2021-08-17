package com.ericlam.mc.eldgui.demo.test;

import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.view.BukkitView;

@UIController("test")
public class TestController {

    public BukkitView<?, ?> index(){
        return new BukkitView<>(TestView.class, null);
    }

}
