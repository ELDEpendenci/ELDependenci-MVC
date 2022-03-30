package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.AddonManager;
import com.ericlam.mc.eld.ELDBukkitAddon;
import com.ericlam.mc.eld.ManagerProvider;
import com.ericlam.mc.eld.ServiceCollection;
import com.ericlam.mc.eld.annotations.ELDPlugin;
import com.ericlam.mc.eldgui.component.factory.*;
import com.ericlam.mc.eldgui.config.ELDGConfig;
import com.ericlam.mc.eldgui.config.ELDGLanguage;
import com.ericlam.mc.eldgui.demo.DemoInventories;
import com.ericlam.mc.eldgui.demo.async.AsyncController;
import com.ericlam.mc.eldgui.demo.error.ErrorController;
import com.ericlam.mc.eldgui.demo.test.TestController;
import com.ericlam.mc.eldgui.demo.user.UserController;

@ELDPlugin(
        lifeCycle = ELDGLifeCycle.class,
        registry = ELDGRegistry.class
)
public final class ELDGPlugin extends ELDBukkitAddon {

    @Override
    protected void bindServices(ServiceCollection serviceCollection) {
        serviceCollection.bindService(InventoryService.class, ELDGInventoryService.class);
        serviceCollection.addSingleton(ManagerFactory.class);
        serviceCollection.addGroupConfiguration(DemoInventories.class);
        serviceCollection.addConfiguration(ELDGLanguage.class);
        serviceCollection.addConfiguration(ELDGConfig.class);
    }


    @Override
    protected void preAddonInstall(ManagerProvider managerProvider, AddonManager addonManager) {
        ELDGMVCInstallation eldgmvcInstallation = new ELDGMVCInstallation(this);
        addonManager.customInstallation(MVCInstallation.class, eldgmvcInstallation);
        // my demo register

        ELDGConfig config = managerProvider.getConfigStorage().getConfigAs(ELDGConfig.class);

        // register controller
        if (config.enableDemo){
            eldgmvcInstallation.registerControllers(
                    UserController.class,
                    ErrorController.class,
                    TestController.class,
                    AsyncController.class
            );
        }

        // register component factory
        eldgmvcInstallation.addComponentFactory(ButtonFactory.class, ELDGButtonFactory.class);
        eldgmvcInstallation.addComponentFactory(TextInputFactory.class, ELDGTextInputFactory.class);
        eldgmvcInstallation.addComponentFactory(NumInputFactory.class, ELDGNumInputFactory.class);
        eldgmvcInstallation.addComponentFactory(AnimatedButtonFactory.class, ELDGAnimatedButtonFactory.class);
        eldgmvcInstallation.addComponentFactory(CheckboxFactory.class, ELDGCheckboxFactory.class);
        eldgmvcInstallation.addComponentFactory(SelectionFactory.class, ELDGSelectionFactory.class);
        eldgmvcInstallation.addComponentFactory(RGBSelectorFactory.class, ELDGRGBSelectorFactory.class);
        eldgmvcInstallation.addComponentFactory(BukkitItemFactory.class, ELDGBukkitItemFactory.class);
        eldgmvcInstallation.addComponentFactory(DateSelectorFactory.class, ELDGDateSelectorFactory.class);
        eldgmvcInstallation.addComponentFactory(TimeSelectorFactory.class, ELDGTimeSelectorFactory.class);

        // install module
        addonManager.installModule(new ELDGMVCModule(eldgmvcInstallation));
    }
}