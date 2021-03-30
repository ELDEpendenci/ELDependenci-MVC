package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.services.ConfigPoolService;
import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.exception.RendererNotFoundException;
import com.ericlam.mc.eldgui.exception.TemplateNotFoundException;
import com.ericlam.mc.eldgui.resources.DemoInventories;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class ELDGFactoryService implements InventoryFactoryService{

    @Inject
    private Map<String, UIRenderer> uiRendererMap;

    @Inject
    private ConfigPoolService configPoolService;

    @Inject
    private ItemStackService itemStackService;

    private final Map<String, UIDispatcher> dispatcherMap = new ConcurrentHashMap<>();

    @Override
    public UIDispatcher getDispatcher(String template, String renderer) throws TemplateNotFoundException, RendererNotFoundException {
        final var identifier = template+"_"+renderer;
        if (dispatcherMap.containsKey(identifier)) return dispatcherMap.get(identifier);
        var temp = configPoolService.getPool(DemoInventories.class);
        if (temp == null || !temp.containsKey(template)) throw new TemplateNotFoundException(template+(temp == null ? "(文件池未加載完成)" : ""));
        var demo = temp.get(template);
        var render = Optional.ofNullable(uiRendererMap.get(renderer)).orElseThrow(() -> new RendererNotFoundException(renderer));
        var dispatcher =  new ELDGDispatcher(demo, render, itemStackService);
        dispatcherMap.put(identifier, dispatcher);
        return dispatcher;
    }

    @Override
    public UIDispatcher getDispatcher(String identifier) throws TemplateNotFoundException, RendererNotFoundException {
        return this.getDispatcher(identifier, identifier);
    }

    @Override
    public UIDispatcher getDispatcher(InventoryTemplate config, String renderer) throws RendererNotFoundException {
        final var identifier = config.getId()+"_"+renderer;
        if (dispatcherMap.containsKey(identifier)) return dispatcherMap.get(identifier);
        var render = Optional.ofNullable(uiRendererMap.get(renderer)).orElseThrow(() -> new RendererNotFoundException(renderer));
        var dispatcher =  new ELDGDispatcher(config, render, itemStackService);
        dispatcherMap.put(identifier, dispatcher);
        return dispatcher;
    }

    @Override
    public UIDispatcher getDispatcher(InventoryTemplate config) throws RendererNotFoundException {
        return this.getDispatcher(config, config.getId());
    }


    @Override
    public UIDispatcher generateInventory(String[][] pattern, String renderer) throws RendererNotFoundException {
        throw new UnsupportedOperationException();
    }
}
