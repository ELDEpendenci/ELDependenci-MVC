package com.ericlam.mc.eldgui.demo;

import com.ericlam.mc.eld.annotations.GroupResource;
import com.ericlam.mc.eldgui.InventoryTemplate;

@GroupResource(
        folder = "templates",
        preloads = {"crafttable", "confirm"}
)
public class DemoInventories extends InventoryTemplate {
}
