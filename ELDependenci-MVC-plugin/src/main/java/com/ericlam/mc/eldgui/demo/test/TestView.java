package com.ericlam.mc.eldgui.demo.test;

import com.ericlam.mc.eldgui.component.DateSelector;
import com.ericlam.mc.eldgui.component.TimeSelector;
import com.ericlam.mc.eldgui.component.factory.ButtonFactory;
import com.ericlam.mc.eldgui.component.factory.DateSelectorFactory;
import com.ericlam.mc.eldgui.component.factory.RGBSelectorFactory;
import com.ericlam.mc.eldgui.component.factory.TimeSelectorFactory;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.View;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Color;
import org.bukkit.Material;

import java.time.LocalDate;
import java.time.LocalTime;

@ViewDescriptor(
        name = "Test GUI",
        rows = 2,
        patterns = {
                "ZZZZZZZZZ",
                "ZZZZZZZZA"
        },
        cancelMove = {'Z', 'A'}
)
public class TestView implements View<Void> {

    @Override
    public void renderView(Void model, UIContext context) {
        ButtonFactory button = context.factory(ButtonFactory.class);

        RGBSelectorFactory rgbSelector = context.factory(RGBSelectorFactory.class);
        DateSelectorFactory dateSelector = context.factory(DateSelectorFactory.class);
        TimeSelectorFactory timeSelector = context.factory(TimeSelectorFactory.class);

        context.pattern('Z')
                .components(
                        rgbSelector
                                .bindInput("testColor", Color.WHITE)
                                .label("&aColor Select: (shift to move color, click to +/-, middle to input)")
                                .create(),
                        dateSelector
                                .bindInput("testDate", LocalDate.now())
                                .label("&aDate Select: (shift move unit, click to +/-, middle to input)")
                                .icon(Material.BEACON)
                                .create(),
                        timeSelector
                                .bindInput("testTime", LocalTime.now())
                                .label("&aTime Select: (shift move unit, click to +/-, middle to input)")
                                .icon(Material.CLOCK)
                                .create()
                )
                .and()
                .pattern('A')
                .components(
                        button.title("&aTest").icon(Material.DIAMOND_BLOCK).create()
                );
    }

}
