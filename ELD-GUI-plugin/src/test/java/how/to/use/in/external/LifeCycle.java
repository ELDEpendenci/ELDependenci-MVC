package how.to.use.in.external;


import com.ericlam.mc.eld.ELDLifeCycle;
import org.bukkit.plugin.java.JavaPlugin;
import tutorial.showcase.ExampleService;

import javax.inject.Inject;

public class LifeCycle implements ELDLifeCycle {

    @Inject // 注入你的服務(API)
    private ExampleService exampleService;

    @Override
    public void onEnable(JavaPlugin javaPlugin) {
        exampleService.doSomethingCool(); // 使用
    }

    @Override
    public void onDisable(JavaPlugin javaPlugin) {

    }
}
