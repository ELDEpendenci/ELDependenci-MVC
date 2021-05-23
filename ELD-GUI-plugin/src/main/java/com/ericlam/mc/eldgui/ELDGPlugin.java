package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.ELDBukkitPlugin;
import com.ericlam.mc.eld.ManagerProvider;
import com.ericlam.mc.eld.ServiceCollection;
import com.ericlam.mc.eld.annotations.ELDPlugin;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.demo.asyncui.AsyncUIController;
import com.ericlam.mc.eldgui.demo.confirm.ConfirmUIController;
import com.ericlam.mc.eldgui.demo.crafttable.CraftTableController;
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
        saveGroupResource("crafttable", "confirm");
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

    private void saveGroupResource(String... ymls){
        for (String yml : ymls) {
            var tempFolder = new File(this.getDataFolder(), "/templates");
            File crafttable = new File(tempFolder, yml+".yml");
            if (!crafttable.exists()) {
                this.saveResource("templates/"+yml+".yml", true);
            }
        }
    }
}
