package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.ELDBukkitPlugin;
import com.ericlam.mc.eld.ManagerProvider;
import com.ericlam.mc.eld.ServiceCollection;
import com.ericlam.mc.eld.annotations.ELDPlugin;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.demo.DemoInventories;
import com.ericlam.mc.eldgui.demo.asyncui.AsyncUIController;
import com.ericlam.mc.eldgui.demo.confirm.ConfirmUIController;
import com.ericlam.mc.eldgui.demo.crafttable.CraftTableController;

import java.io.File;
import java.util.Map;

@ELDPlugin(
        lifeCycle = ELDGLifeCycle.class,
        registry = ELDGRegistry.class
)
public class ELDGPlugin extends ELDBukkitPlugin {

    @Override
    protected void bindServices(ServiceCollection serviceCollection) {
        serviceCollection.bindService(InventoryService.class, ELDGInventoryService.class);
        // demo
        serviceCollection.addServices(UIController.class, Map.of(
                "crafttable", CraftTableController.class,
                "confirm", ConfirmUIController.class,
                "async", AsyncUIController.class
        ));
        serviceCollection.addSingleton(ManagerFactory.class);
        serviceCollection.addGroupConfiguration(DemoInventories.class);
        serviceCollection.addConfiguration(ELDGLanguage.class);

    }

    @Override
    protected void manageProvider(ManagerProvider managerProvider) {

    }
}
