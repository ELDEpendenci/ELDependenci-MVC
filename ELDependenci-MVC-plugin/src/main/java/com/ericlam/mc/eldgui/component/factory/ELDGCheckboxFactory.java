package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.AttributeController;
import com.ericlam.mc.eldgui.component.Checkbox;
import com.ericlam.mc.eldgui.component.Component;
import org.bukkit.Material;

public final class ELDGCheckboxFactory extends AbstractComponentFactory<CheckboxFactory> implements CheckboxFactory {

    private Material checkedIcon, uncheckedIcon;
    private String checkedShow, uncheckedShow;
    private boolean disabled;


    public ELDGCheckboxFactory(ItemStackService itemStackService, AttributeController attributeController) {
        super(itemStackService, attributeController);
    }

    @Override
    protected void defaultProperties() {
        this.checkedIcon = Material.EMERALD_BLOCK;
        this.uncheckedIcon = Material.REDSTONE_BLOCK;
        this.checkedShow = "&a[x]";
        this.uncheckedShow = "&c[]";
        this.disabled = false;
    }

    @Override
    public Component build(ItemStackService.ItemFactory itemFactory) {
        return new Checkbox(attributeController, itemFactory, checkedIcon, uncheckedIcon, checkedShow, uncheckedShow, disabled);
    }

    @Override
    public CheckboxFactory bindInput(String field, boolean initValue) {
        bind(AttributeController.VALUE_TAG, field);
        bind(AttributeController.FIELD_TAG, initValue);
        return editItemByFactory(f -> f.lore("-> "+ (initValue ? checkedShow : uncheckedShow)));
    }

    @Override
    public CheckboxFactory checked(Material material) {
        this.checkedIcon = material;
        return this;
    }

    @Override
    public CheckboxFactory unchecked(Material material) {
        this.uncheckedIcon = material;
        return this;
    }

    @Override
    public CheckboxFactory checkedDisplay(String show) {
        this.checkedShow = show;
        return this;
    }

    @Override
    public CheckboxFactory uncheckedDisplay(String show) {
        this.uncheckedShow = show;
        return this;
    }

    @Override
    public CheckboxFactory disabled() {
        this.disabled = true;
        return editItemByFactory(f -> f.lore("&cDisabled"));
    }
}
