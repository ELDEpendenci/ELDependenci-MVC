package me.oska.gui;

import com.ericlam.mc.eld.ELDBukkitPlugin;
import com.ericlam.mc.eld.ManagerProvider;
import com.ericlam.mc.eld.ServiceCollection;
import com.ericlam.mc.eld.annotations.ELDPlugin;
import me.oska.gui.service.ELDInventoryService;
import me.oska.gui.service.InventoryService;

import javax.inject.Inject;

@ELDPlugin(
        lifeCycle = InventoryLifecycle.class,
        registry = InventoryRegistry.class
)
public class InventoryGUI extends ELDBukkitPlugin {

    @Inject
    private InventoryService service;

    @Override
    protected void bindServices(ServiceCollection serviceCollection) {
        serviceCollection.bindService(InventoryService.class, ELDInventoryService.class);
        serviceCollection.addServices(InventoryUI.class, service.getRenderers());
    }

    @Override
    protected void manageProvider(ManagerProvider managerProvider) {

    }
}

