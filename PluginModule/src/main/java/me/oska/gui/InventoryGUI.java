package me.oska.gui;

import com.ericlam.mc.eld.ELDBukkitPlugin;
import com.ericlam.mc.eld.ManagerProvider;
import com.ericlam.mc.eld.ServiceCollection;
import com.ericlam.mc.eld.annotations.ELDPlugin;

@ELDPlugin(
        lifeCycle = InventoryLifecycle.class,
        registry = InventoryRegistry.class
)
public class InventoryGUI extends ELDBukkitPlugin {

    @Override
    protected void bindServices(ServiceCollection serviceCollection) {
    }

    @Override
    protected void manageProvider(ManagerProvider managerProvider) {

    }
}

