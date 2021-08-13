package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;

public interface ButtonFactory extends ComponentFactory<ButtonFactory> {

    ButtonFactory title(String title);

    ButtonFactory lore(String... lore);

    ButtonFactory number(int amount);

}
