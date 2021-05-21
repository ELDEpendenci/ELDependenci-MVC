package com.ericlam.mc.eldgui;

import com.ericlam.mc.eldgui.view.ViewDescriptor;

import java.util.Arrays;
import java.util.Map;

public final class CodeInventoryTemplate extends InventoryTemplate {

    public CodeInventoryTemplate(ViewDescriptor viewDescriptor){
        this.name = viewDescriptor.name();
        this.rows = viewDescriptor.rows();
        this.pattern = Arrays.asList(viewDescriptor.patterns());
        this.items = Map.of();
    }

}
