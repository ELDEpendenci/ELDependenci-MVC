package com.ericlam.mc.eldgui.command;

import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.eld.components.BukkitCommand;
import org.bukkit.command.CommandSender;

@Commander(
        name = "eldg",
        description = "eld-gui 主要指令"
)
public class ELDGCommand implements BukkitCommand {
    @Override
    public void execute(CommandSender commandSender) {
    }
}
