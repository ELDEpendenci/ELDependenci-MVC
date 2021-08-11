package com.ericlam.mc.eldgui;

import com.google.inject.Injector;

import javax.inject.Inject;
import javax.inject.Named;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public class ELDGInventoryService implements InventoryService {

    private final Map<String, UIDispatcher> uiDispatcherMap = new ConcurrentHashMap<>();

    private final Map<String, Class<?>> uiControllerMap;

    @Inject
    public ELDGInventoryService(ELDGMVCInstallation installation){
        this.uiControllerMap = installation.getControllerMap();
    }

    @Inject
    private ELDGLanguage language;
    @Inject
    private Injector injector;

    @Override
    public UIDispatcher getUIDispatcher(String name) throws UINotFoundException {
        if (uiDispatcherMap.containsKey(name)) return uiDispatcherMap.get(name);
        Class<?> controllerCls = Optional.ofNullable(uiControllerMap.get(name)).orElseThrow(() -> new UINotFoundException(MessageFormat.format(language.getLang().get("not-found"), name)));
        Object controller = injector.getInstance(controllerCls);
        UIDispatcher dispatcher = new ELDGDispatcher(controller, (s, player, ui) -> this.getUIDispatcher(ui).openFor(player));
        injector.injectMembers(dispatcher);
        this.uiDispatcherMap.put(name, dispatcher);
        return dispatcher;
    }

    public synchronized void onClose() {
        this.uiDispatcherMap.values().forEach(d -> ((ELDGDispatcher) d).onClose());
    }
}
