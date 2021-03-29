package how.to.use.in.external;


import com.ericlam.mc.eld.ELDLifeCycle;
import com.ericlam.mc.eldgui.InventoryFactoryService;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

public class LifeCycle implements ELDLifeCycle {

    @Inject
    private InventoryFactoryService invServices;

    @Override
    public void onEnable(JavaPlugin javaPlugin) {

    }

    @Override
    public void onDisable(JavaPlugin javaPlugin) {

    }
}
