package com.ericlam.mc.eldgui.controller;

import com.ericlam.mc.eldgui.view.BukkitView;
import com.ericlam.mc.eldgui.view.View;

public interface ViewLifeCycleHook {

    void postUpdateView(Class<View<?>> view);

    void preViewDestroy(Class<View<?>> view);

}
