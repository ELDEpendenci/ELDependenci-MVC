package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;
import org.bukkit.Color;

public interface RGBSelectorFactory extends ComponentFactory<RGBSelectorFactory> {

    RGBSelectorFactory label(String title);

    RGBSelectorFactory bindInput(String field, Color color);

    RGBSelectorFactory inputMessage(String message);

    RGBSelectorFactory invalidMessage(String message);

    RGBSelectorFactory waitForInput(long maxWait);

    RGBSelectorFactory disabled();

}
