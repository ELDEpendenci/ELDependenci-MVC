package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.ELDBukkitPlugin;
import com.ericlam.mc.eld.ManagerProvider;
import com.ericlam.mc.eld.ServiceCollection;
import com.ericlam.mc.eld.annotations.ELDPlugin;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.demo.CraftTableController;
import com.ericlam.mc.eldgui.demo.DemoInventories;

import java.io.File;
import java.util.Map;

@ELDPlugin(
        lifeCycle = ELDGLifeCycle.class,
        registry = ELDGRegistry.class
)
public class ELDGPlugin extends ELDBukkitPlugin {

    @Override
    protected void bindServices(ServiceCollection serviceCollection) {
        var tempFolder = new File(this.getDataFolder(), "/templates");
        File crafttable = new File(tempFolder, "crafttable.yml");
        if (!crafttable.exists()) {
            this.saveResource("templates/crafttable.yml", true);
        }
        serviceCollection.bindService(InventoryService.class, ELDGInventoryService.class);
        serviceCollection.addServices(UIController.class, Map.of(
                "crafttable", CraftTableController.class // demo
        ));
        serviceCollection.addSingleton(MethodParseFactory.class);
        serviceCollection.addGroupConfiguration(DemoInventories.class);
        serviceCollection.addConfiguration(ELDGLanguage.class);

    }

    @Override
    protected void manageProvider(ManagerProvider managerProvider) {

    }
}
