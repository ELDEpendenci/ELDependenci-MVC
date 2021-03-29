package com.ericlam.mc.eldgui.commands;

import com.ericlam.mc.eld.annotations.CommandArg;
import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.eld.components.CommandNode;
import com.ericlam.mc.eldgui.InventoryFactoryService;
import com.ericlam.mc.eldgui.exceptions.RendererNotFoundException;
import com.ericlam.mc.eldgui.exceptions.TemplateNotFoundException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.HashMap;

@Commander(
        name = "demo",
        description = "打開界面demo指令",
        playerOnly = true
)
public class ELDGDemoCommand implements CommandNode {

    @Inject
    private InventoryFactoryService factoryService;

    @CommandArg(order = 1)
    private String demo;

    @CommandArg(order = 1, optional = true)
    private String renderer;

    @Override
    public void execute(CommandSender commandSender) {
       var player = (Player) commandSender;
        try {
            var ui = factoryService.getDispatcher(demo, renderer == null ? demo : renderer);
            ui.forward(player);
        } catch (TemplateNotFoundException | RendererNotFoundException e) {
            var reason = e instanceof TemplateNotFoundException ? "template" : "renderer";
            var arg = e instanceof TemplateNotFoundException ? demo : renderer;
            player.sendMessage("§c找不到 "+reason+": "+arg);
        }
    }
}
