package com.ericlam.mc.eldgui;

import com.ericlam.mc.eldgui.event.MethodParseManager;
import org.bukkit.event.inventory.InventoryEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class MethodParseFactory {

    private final Map<Class<? extends InventoryEvent>, Class<? extends MethodParseManager<? extends InventoryEvent>>> parseManagerMap = new ConcurrentHashMap<>();

    public <T extends InventoryEvent, E extends T> void registerParseManager(Class<E> cls, Class<? extends MethodParseManager<E>> parseCls) {
        this.parseManagerMap.put(cls, parseCls);
    }

    @SuppressWarnings("unchecked")
    public <E extends InventoryEvent> MethodParseManager<E> buildParseManager(Class<E> eventClass, Consumer<MethodParseManager<E>> parseConsumer) {
        var parser = Optional.ofNullable(this.parseManagerMap.get(eventClass)).orElseThrow(() -> new IllegalStateException("unknown parser for class: "+eventClass));
        try {
            var c = parser.getConstructor();
            var parserManager = (MethodParseManager<E>) c.newInstance();
            parseConsumer.accept(parserManager);
            return parserManager;
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("cannot find empty arg constructor to new instance.", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

}
