package tutorial.showcase;

import org.bukkit.Bukkit;

public class ExampleServiceImpl implements ExampleService {

    @Override
    public void doSomethingCool() {
        Bukkit.getLogger().info("Hello World!!");
    }
}
