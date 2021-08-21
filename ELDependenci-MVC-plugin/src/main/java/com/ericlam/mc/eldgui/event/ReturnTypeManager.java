package com.ericlam.mc.eldgui.event;

import org.bukkit.Bukkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ReturnTypeManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReturnTypeManager.class);
    private final Map<Function<Type, Boolean>, BiConsumer<Object, Annotation[]>> supplierMap = new ConcurrentHashMap<>();

    public void registerReturnType(Function<Type, Boolean> define, BiConsumer<Object, Annotation[]> consumer) {
        supplierMap.put(define, consumer);
    }

    public boolean handleReturnResult(Method method, Object result) {
        if (method == null || result == null) return false;
        final Annotation[] annotations = method.getDeclaredAnnotations();
        final Type returnType = method.getGenericReturnType();
        return this.handleReturnResult(returnType, result, annotations);
    }

    public boolean handleReturnResult(Type returnType, Object result) {
        if (result == null) return false;
        final Annotation[] annotations = new Annotation[0];
        return this.handleReturnResult(returnType, result, annotations);
    }

    public boolean handleReturnResult(Type returnType, Object result, Annotation[] annotations) {
        if (result == null) return false;
        return supplierMap.keySet().stream()
                .filter(key -> key.apply(returnType))
                .findFirst()
                .map(key -> {
                    supplierMap.get(key).accept(result, annotations);
                    return true;
                }).orElseGet(() -> {
                    LOGGER.warn("Unknown return type " + returnType + ", ignore handling.");
                    return false;
                });
    }

}
