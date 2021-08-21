package com.ericlam.mc.eldgui.demo.async;

import com.ericlam.mc.eld.services.ScheduleService;
import com.ericlam.mc.eldgui.ELDGPlugin;
import com.ericlam.mc.eldgui.controller.AsyncLoadingView;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.event.ClickMapping;
import com.ericlam.mc.eldgui.view.BukkitRedirectView;
import com.ericlam.mc.eldgui.view.BukkitView;
import org.bukkit.entity.Player;

import javax.inject.Inject;


@UIController("async")
public final class AsyncController {

    @Inject
    private ScheduleService scheduleService;

    @Inject
    private ELDGPlugin plugin;

    public ScheduleService.BukkitPromise<BukkitView<?, ?>> index(){
        return scheduleService.runAsync(plugin, () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).thenApplySync(v -> new BukkitView<>(AsyncView.class, null));
    }


    @AsyncLoadingView(CustomLoadingView.class)
    @ClickMapping(view = AsyncView.class, pattern = 'A')
    public ScheduleService.BukkitPromise<BukkitView<?, ?>> onClick(Player player){
        player.sendMessage("3 seconds to go to the user view.");
        return scheduleService.runAsync(plugin, () -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).thenApplySync(v -> new BukkitRedirectView("user"));
    }
}
