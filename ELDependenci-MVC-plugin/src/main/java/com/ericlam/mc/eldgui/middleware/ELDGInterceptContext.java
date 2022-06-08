package com.ericlam.mc.eldgui.middleware;

import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.view.BukkitView;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class ELDGInterceptContext implements InterceptContext {

    private BukkitView<?, ?> view;

    private final Player player;
    private final UISession session;

    public ELDGInterceptContext(Player player, UISession session) {
        this.player = player;
        this.session = session;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public UISession getSession() {
        return session;
    }

    @Override
    public void setRedirect(BukkitView<?, ?> view) {
        this.view = view;
    }

    @Nullable
    public BukkitView<?, ?> getView() {
        return view;
    }
}
