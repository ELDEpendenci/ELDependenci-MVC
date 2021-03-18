package how.to.use.in.external;

import com.ericlam.mc.eld.services.ItemStackService;
import me.oska.gui.InventoryUI;
import me.oska.gui.service.InventoryService;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestGUI {

    @Inject
    private InventoryService inventoryService;

    @Inject
    private ItemStackService itemStackService;

    private final Map<Player, InventoryUI<Prop, State>> playerInventoryUIMap = new HashMap<>();

    private static class Prop{

    }

    private static class State{
        public String state = "none";
        public int number = 5;
    }

    public InventoryUI<Prop, State> getUI(Player player){
        if (playerInventoryUIMap.containsKey(player)) return playerInventoryUIMap.get(player);
        var jumpGUI = inventoryService.buildInventory(new Object(), new Object());
        jumpGUI.set(Set.of(0), itemStackService.build(Material.APPLE).getItem());
        var ui = inventoryService.buildInventory(new Prop(), new State());
        ui.setOnRender((p, s) ->{
            var item = itemStackService.build(Material.PAPER)
                    .amount(s.number)
                    .display("item state: "+s.state)
                    .getItem();
            ui.set(Set.of(0), item, e -> {
                ui.updateState(ss -> {
                    ss.number += 1;
                    s.state += ("no. " + ss.number);
                });
            });
            var jump = itemStackService.build(Material.APPLE)
                    .display("jump to another page")
                    .getItem();
            ui.set(Set.of(1), jump, jumpGUI);
        });

        playerInventoryUIMap.put(player, ui);
        return getUI(player);
    }
}
