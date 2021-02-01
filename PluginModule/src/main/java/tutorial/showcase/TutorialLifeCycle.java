package tutorial.showcase;

import com.ericlam.mc.eld.ELDLifeCycle;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

public class TutorialLifeCycle implements ELDLifeCycle {

    @Inject
    private ExampleService service;

    @Override
    public void onEnable(JavaPlugin javaPlugin) {
        service.doSomethingCool();
    }

    @Override
    public void onDisable(JavaPlugin javaPlugin) {
        service.doSomethingCool();
    }
}
