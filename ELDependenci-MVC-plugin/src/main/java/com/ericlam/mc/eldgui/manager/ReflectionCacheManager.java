package com.ericlam.mc.eldgui.manager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ReflectionCacheManager {
    private final Map<Method, Annotation[]> methodAnnotationMap = new HashMap<>();
    private final Map<Class<?>, Annotation[]> classAnnotationMap = new HashMap<>();
    private final Map<Class<?>, Method[]> classMethodsMap = new HashMap<>();

    private final Map<Method, Annotation[][]> methodParameterAnnotationMap = new HashMap<>();

    private final Map<Method, Type[]> methodParameterTypeMap = new HashMap<>();

    public Annotation[] getDeclaredAnnotations(Method method) {
        return methodAnnotationMap.computeIfAbsent(method, Method::getDeclaredAnnotations);
    }

    public Annotation[] getDeclaredAnnotations(Class<?> clazz) {
        return classAnnotationMap.computeIfAbsent(clazz, Class::getDeclaredAnnotations);
    }

    public Method[] getMethods(Class<?> clazz) {
        return classMethodsMap.computeIfAbsent(clazz, Class::getMethods);
    }

    public Annotation[][] getParameterAnnotations(Method method) {
        return methodParameterAnnotationMap.computeIfAbsent(method, Method::getParameterAnnotations);
    }

    public Type[] getParameterTypes(Method method) {
        return methodParameterTypeMap.computeIfAbsent(method, Method::getGenericParameterTypes);
    }
}
