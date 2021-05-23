package com.ericlam.mc.eldgui;

import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public final class CodeInventoryTemplate extends InventoryTemplate {

    public CodeInventoryTemplate(ViewDescriptor viewDescriptor){
        this.name = viewDescriptor.name();
        this.rows = viewDescriptor.rows();
        this.pattern = Arrays.asList(viewDescriptor.patterns());
        this.items = new LinkedHashMap<>();
        for (char c : viewDescriptor.cancelMove()) {
            var d = new ItemDescriptor();
            d.material = Material.AIR;
            d.cancelMove = true;
            this.items.put(String.valueOf(c), d);
        }
    }

}
