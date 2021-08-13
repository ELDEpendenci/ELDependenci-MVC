package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.registrations.CommandRegistry;
import com.ericlam.mc.eld.registrations.ComponentsRegistry;
import com.ericlam.mc.eld.registrations.ListenerRegistry;
import com.ericlam.mc.eldgui.command.ELDGCommand;
import com.ericlam.mc.eldgui.command.ELDGDemoCommand;

public class ELDGRegistry implements ComponentsRegistry {

    @Override
    public void registerCommand(CommandRegistry commandRegistry) {
        commandRegistry.command(ELDGCommand.class, c -> {
            c.command(ELDGDemoCommand.class);
        });
    }

    @Override
    public void registerListeners(ListenerRegistry listenerRegistry) {
        // no listeners
    }

}
