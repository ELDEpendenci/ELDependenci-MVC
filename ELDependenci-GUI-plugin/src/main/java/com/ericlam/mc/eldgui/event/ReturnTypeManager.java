package com.ericlam.mc.eldgui.event;

import org.bukkit.Bukkit;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ReturnTypeManager {

    private final Map<Function<Type, Boolean>, Consumer<Object>> supplierMap = new ConcurrentHashMap<>();

    public void registerReturnType(Function<Type, Boolean> define, Consumer<Object> consumer) {
        supplierMap.put(define, consumer);
    }

    public boolean handleReturnResult(Type returnType, Object result) {
        if (result == null) return false;
        return supplierMap.keySet().stream()
                .filter(key -> key.apply(returnType))
                .findFirst()
                .map(key -> {
                    supplierMap.get(key).accept(result);
                    return true;
                }).orElseGet(() -> {
                    Bukkit.getLogger().warning("[ELDepdendenci-GUI] Unknown return type " + returnType + ", ignore handling.");
                    return false;
                });
    }

}
