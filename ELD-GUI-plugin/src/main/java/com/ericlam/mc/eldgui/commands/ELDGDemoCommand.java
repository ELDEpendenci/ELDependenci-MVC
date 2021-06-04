package com.ericlam.mc.eldgui.commands;

import com.ericlam.mc.eld.annotations.CommandArg;
import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.eld.components.CommandNode;
import com.ericlam.mc.eldgui.InventoryService;
import com.ericlam.mc.eldgui.UINotFoundException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

@Commander(
        name = "demo",
        description = "打開界面demo指令",
        playerOnly = true
)
public class ELDGDemoCommand implements CommandNode {

    @Inject
    private InventoryService inventoryService;

    @CommandArg(order = 1)
    private String gui;

    @Override
    public void execute(CommandSender commandSender) {
        var player = (Player) commandSender;
        try {
            inventoryService.getUIDispatcher(gui).openFor(player);
        } catch (UINotFoundException e) {
            player.sendMessage(e.getMessage());
        }
    }
}
