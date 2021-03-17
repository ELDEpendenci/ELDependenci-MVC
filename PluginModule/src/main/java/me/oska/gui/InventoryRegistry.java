package me.oska.gui;

import com.ericlam.mc.eld.registrations.CommandRegistry;
import com.ericlam.mc.eld.registrations.ComponentsRegistry;
import com.ericlam.mc.eld.registrations.ListenerRegistry;
import me.oska.gui.listener.InventoryListener;

import java.util.Collections;
import java.util.List;

public class InventoryRegistry implements ComponentsRegistry {

    @Override
    public void registerCommand(CommandRegistry commandRegistry) {
        // no command
    }

    @Override
    public void registerListeners(ListenerRegistry listenerRegistry) {
        listenerRegistry.listeners(List.of(InventoryListener.class));
    }

}
