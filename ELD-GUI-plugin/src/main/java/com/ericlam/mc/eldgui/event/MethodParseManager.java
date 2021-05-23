package com.ericlam.mc.eldgui.event;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class MethodParseManager {

    private final Map<BiFunction<Type, Annotation[], Boolean>, Function<Annotation[], Object>> supplierMap = new ConcurrentHashMap<>();

    public void registerParser(BiFunction<Type, Annotation[], Boolean> define, Function<Annotation[], Object> supplier) {
        supplierMap.put(define, supplier);
    }

    public Object[] getMethodParameters(Method method) {
        Type[] paras = method.getGenericParameterTypes();
        Object[] results = new Object[paras.length];
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < paras.length; i++) {
            Type cls = paras[i];
            Annotation[] annos = annotations[i];
            var annoStr = Arrays.stream(annos).map(a -> a.annotationType().getName()).collect(Collectors.joining(", "));
            BiFunction<Type, Annotation[], Boolean> key = null;
            for (BiFunction<Type, Annotation[], Boolean> k : supplierMap.keySet()) {
                if (k.apply(cls, annos)) {
                    key = k;
                    break;
                }
            }
            if (key == null)
                throw new IllegalArgumentException("unknown parameter " + cls + " with annotations: " + annoStr);
            var result = supplierMap.get(key).apply(annos);
            if (result == null)
                throw new IllegalArgumentException("the result of " + cls + " is null with annotations: " + annoStr);
            results[i] = result;
        }
        return results;
    }


}
