package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.bukkit.CommandNode;
import com.ericlam.mc.eld.bukkit.ComponentsRegistry;
import com.ericlam.mc.eld.registration.CommandRegistry;
import com.ericlam.mc.eld.registration.ListenerRegistry;
import com.ericlam.mc.eldgui.command.ELDGCommand;
import com.ericlam.mc.eldgui.command.ELDGDemoCommand;
import org.bukkit.event.Listener;

public final class ELDGRegistry implements ComponentsRegistry {


    @Override
    public void registerCommand(CommandRegistry<CommandNode> commandRegistry) {
        commandRegistry.command(ELDGCommand.class, c -> {
            c.command(ELDGDemoCommand.class);
        });
    }

    @Override
    public void registerListeners(ListenerRegistry<Listener> listenerRegistry) {

    }
}
