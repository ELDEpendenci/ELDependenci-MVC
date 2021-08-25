package com.ericlam.mc.eldgui.controller;

import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.view.View;
import org.bukkit.entity.Player;

public interface ViewLifeCycleHook {

    void postUpdateView(Player player, Class<View<?>> view, UISession session);

    void preViewDestroy(Player player, Class<View<?>> view, UISession session);

}
