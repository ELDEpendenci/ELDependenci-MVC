package com.ericlam.mc.eldgui.demo.test;

import com.ericlam.mc.eldgui.controller.ModelAttribute;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.event.ClickMapping;
import com.ericlam.mc.eldgui.lifecycle.PostConstruct;
import com.ericlam.mc.eldgui.lifecycle.PreDestroy;
import com.ericlam.mc.eldgui.view.BukkitView;
import org.bukkit.entity.Player;

@UIController("test")
public class TestController {

    @PostConstruct
    public void beforeCreate(Player player){
        player.sendMessage("life cycle: before create for test controller");
    }

    public BukkitView<?, ?> index(){
        return new BukkitView<>(TestView.class);
    }


    @ClickMapping(view = TestView.class, pattern = 'A')
    public void onClick(@ModelAttribute('Z') TestModel test, Player player){
        player.sendMessage(test.toString());
    }


    @PreDestroy
    public void beforeDestroy(Player player){
        player.sendMessage("life cycle: before destroy for test controller");
    }
}
