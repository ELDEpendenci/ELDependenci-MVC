package com.ericlam.mc.eldgui.demo.asyncui;

import com.ericlam.mc.eld.services.ScheduleService;
import com.ericlam.mc.eldgui.ELDGPlugin;
import com.ericlam.mc.eldgui.LiveData;
import com.ericlam.mc.eldgui.controller.ControllerForView;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.lifecycle.OnRendered;

import javax.inject.Inject;

@ControllerForView(AsyncUIView.class)
public class AsyncUIController implements UIController {

    @Inject
    private ScheduleService scheduleService;

    @Inject
    private ELDGPlugin eldgPlugin;

    @OnRendered
    public void onRendered(LiveData<AsyncUIModel> asyncUIModelLiveData){
        scheduleService.callAsync(eldgPlugin, () -> {
            Thread.sleep(5000);
            return "Hello World!!";
        }).thenRunSync(result -> asyncUIModelLiveData.update(m -> m.setTextFromAsync(result))).join();
    }
}
