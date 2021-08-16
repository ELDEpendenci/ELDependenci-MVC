package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;
import org.bukkit.Material;

public interface CheckboxFactory extends ComponentFactory<CheckboxFactory> {

    CheckboxFactory bindInput(String field, boolean initValue);

    CheckboxFactory checked(Material material);

    CheckboxFactory unchecked(Material material);

    CheckboxFactory checkedDisplay(String show);

    CheckboxFactory uncheckedDisplay(String show);

    CheckboxFactory disabled();

}
