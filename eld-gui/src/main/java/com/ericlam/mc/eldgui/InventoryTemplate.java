package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.components.GroupConfiguration;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class InventoryTemplate extends GroupConfiguration {

    public String name;

    public int rows;

    public List<String> pattern;

    public Map<String, ItemDescriptor> items;

    public static class ItemDescriptor{

        public Material material = Material.STONE;

        public String name = "";

        public int amount = 1;

        public List<String> lore = new ArrayList<>();

        public boolean glowing = false;

        public boolean cancelMove = true;

    }
}

