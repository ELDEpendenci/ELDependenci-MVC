package me.oska.gui;

import com.ericlam.mc.eld.ELDBukkitPlugin;
import com.ericlam.mc.eld.ManagerProvider;
import com.ericlam.mc.eld.ServiceCollection;
import com.ericlam.mc.eld.annotations.ELDPlugin;
import me.oska.gui.service.InventoryService;
import me.oska.gui.service.ELDInventoryService;

@ELDPlugin(
        lifeCycle = InventoryLifecycle.class,
        registry = InventoryRegistry.class
)
public class InventoryGUI extends ELDBukkitPlugin {

    @Override
    protected void bindServices(ServiceCollection serviceCollection) {
        serviceCollection.bindService(InventoryService.class, ELDInventoryService.class);
    }

    @Override
    protected void manageProvider(ManagerProvider managerProvider) {

    }
}

