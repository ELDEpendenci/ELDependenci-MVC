package com.ericlam.mc.eldgui.exception;

import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.view.BukkitView;
import org.bukkit.entity.Player;

public interface ExceptionViewHandler {

    BukkitView<?, ?> createErrorView(Exception exception, String fromController, UISession session, Player player);

}
