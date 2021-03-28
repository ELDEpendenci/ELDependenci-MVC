package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.ELDBukkitPlugin;
import com.ericlam.mc.eld.ManagerProvider;
import com.ericlam.mc.eld.ServiceCollection;
import com.ericlam.mc.eld.annotations.ELDPlugin;

@ELDPlugin(
        lifeCycle = ELDGLifeCycle.class,
        registry = ELDGRegistry.class
)
public class ELDGPlugin extends ELDBukkitPlugin {

    @Override
    protected void bindServices(ServiceCollection serviceCollection) {
    }

    @Override
    protected void manageProvider(ManagerProvider managerProvider) {

    }
}
