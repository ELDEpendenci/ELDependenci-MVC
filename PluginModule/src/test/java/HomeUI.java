import me.oska.gui.InventoryUI;
import me.oska.gui.service.InventoryService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

public class HomeUI implements InventoryUI<HomeUI.State> {

    @Inject
    private InventoryService service;

    public static class State {
        public int clicked;
    }

    @Override
    public Inventory createInventory(HomeUI.State state) {
        // component did mount
        return Bukkit.createInventory(null, 54, "Some title");
    }

    @Override
    public void render(Inventory inventory, HomeUI.State props) {
        // render
        inventory.setItem(props.clicked, new ItemStack(Material.BIRCH_WOOD));
    }

    @Override
    public void click(InventoryClickEvent event, HomeUI.State props) {
        props.clicked = event.getSlot();
        service.holder(event.getWhoClicked()).reload(props);
    }

    @Override
    public void dispose() {
        // component unmount
    }


}
