package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.ELDBukkitPlugin;
import com.ericlam.mc.eld.ManagerProvider;
import com.ericlam.mc.eld.ServiceCollection;
import com.ericlam.mc.eld.annotations.ELDPlugin;
import com.ericlam.mc.eldgui.resources.DemoInventories;
import com.ericlam.mc.eldgui.testdemo.AppleShopRenderer;
import com.ericlam.mc.eldgui.testdemo.CraftTableRenderer;

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
        File appleShop = new File(tempFolder, "apple-shop.yml");
        if (!appleShop.exists()) {
            this.saveResource("templates/apple-shop.yml", true);
        }
        File crafttable = new File(tempFolder, "crafttable.yml");
        if (!crafttable.exists()) {
            this.saveResource("templates/crafttable.yml", true);
        }

        serviceCollection.addGroupConfiguration(DemoInventories.class);
        serviceCollection.bindService(InventoryFactoryService.class, ELDGFactoryService.class);
        serviceCollection.addServices(UIRenderer.class, Map.of(
                "apple-shop", AppleShopRenderer.class,
                "crafttable", CraftTableRenderer.class
        ));
    }

    @Override
    protected void manageProvider(ManagerProvider managerProvider) {

    }
}
