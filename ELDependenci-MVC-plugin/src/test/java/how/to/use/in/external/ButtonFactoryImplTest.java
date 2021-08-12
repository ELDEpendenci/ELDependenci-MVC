package how.to.use.in.external;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.Component;
import com.ericlam.mc.eldgui.component.factory.ButtonFactory;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class ButtonFactoryImplTest implements ButtonFactory {

    @Override
    public ButtonFactory icon(Material material) {
        return null;
    }

    @Override
    public ButtonFactory title(String title) {
        return null;
    }

    @Override
    public ButtonFactory lore(String... lore) {
        return null;
    }

    @Override
    public ButtonFactory bind(String key, Object value) {
        return null;
    }

    @Override
    public ButtonFactory editByItemFactory(ItemStackService.ItemFactory itemFactory) {
        return null;
    }

    @Override
    public Component create() {
        return null;
    }

    @Override
    public ButtonFactory onClick(Consumer<InventoryClickEvent> clickEventConsumer) {
        return null;
    }
}
