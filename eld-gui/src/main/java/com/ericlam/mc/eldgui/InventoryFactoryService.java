package com.ericlam.mc.eldgui;

import com.ericlam.mc.eldgui.exception.RendererNotFoundException;
import com.ericlam.mc.eldgui.exception.TemplateNotFoundException;

public interface InventoryFactoryService {

    UIDispatcher getDispatcher(String template, String renderer) throws TemplateNotFoundException, RendererNotFoundException;

    UIDispatcher getDispatcher(String identifier) throws TemplateNotFoundException, RendererNotFoundException;

    UIDispatcher getDispatcher(InventoryTemplate config, String renderer) throws RendererNotFoundException;

    UIDispatcher getDispatcher(InventoryTemplate config) throws RendererNotFoundException;

    UIDispatcher generateInventory(String[][] pattern, String renderer) throws RendererNotFoundException;

}
