package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.AddonManager;
import com.ericlam.mc.eld.ELDBukkitAddon;
import com.ericlam.mc.eld.ManagerProvider;
import com.ericlam.mc.eld.ServiceCollection;
import com.ericlam.mc.eld.annotations.ELDPlugin;
import com.ericlam.mc.eldgui.demo.DemoInventories;
import com.ericlam.mc.eldgui.demo.user.UserController;

@ELDPlugin(
        lifeCycle = ELDGLifeCycle.class,
        registry = ELDGRegistry.class
)
public class ELDGPlugin extends ELDBukkitAddon {

    @Override
    protected void bindServices(ServiceCollection serviceCollection) {
        serviceCollection.bindService(InventoryService.class, ELDGInventoryService.class);
        // demo
        serviceCollection.addSingleton(ManagerFactory.class);
        serviceCollection.addGroupConfiguration(DemoInventories.class);
        serviceCollection.addConfiguration(ELDGLanguage.class);
    }


    @Override
    protected void preAddonInstall(ManagerProvider managerProvider, AddonManager addonManager) {
        ELDGMVCInstallation eldgmvcInstallation = new ELDGMVCInstallation(this);
        addonManager.customInstallation(MVCInstallation.class, eldgmvcInstallation);
        // my demo register
        eldgmvcInstallation.registerControllers(UserController.class);
        //
        addonManager.installModule(new ELDGMVCModule(eldgmvcInstallation));
    }
}
