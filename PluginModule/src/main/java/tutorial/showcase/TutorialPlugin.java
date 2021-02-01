package tutorial.showcase;

import com.ericlam.mc.eld.ELDBukkitPlugin;
import com.ericlam.mc.eld.ManagerProvider;
import com.ericlam.mc.eld.ServiceCollection;
import com.ericlam.mc.eld.annotations.ELDPlugin;

@ELDPlugin(
        lifeCycle = TutorialLifeCycle.class,
        registry = TutorialRegistry.class
)
public class TutorialPlugin extends ELDBukkitPlugin {

    @Override
    protected void bindServices(ServiceCollection serviceCollection) {
        serviceCollection.bindService(ExampleService.class, ExampleServiceImpl.class);
    }

    @Override
    protected void manageProvider(ManagerProvider managerProvider) {

    }
}
