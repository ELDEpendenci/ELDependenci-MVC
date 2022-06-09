package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.ELDependenci;
import com.ericlam.mc.eld.services.ReflectionService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class PersistDataUtils {

    public static class ObjectMapperGetter {

        @Inject
        @Named("eld-json")
        private ObjectMapper mapper;

        public ObjectMapper getMapper() {
            return mapper;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistDataUtils.class);

    private static final Supplier<ObjectMapper> MAPPER_GETTER = () -> {
        var getter = ELDependenci.getApi().exposeService(ObjectMapperGetter.class);
        return getter.getMapper();
    };

    private static final Gson GSON = new Gson();

    public static final PersistentDataType<byte[], Object> GENERIC_DATA_TYPE = new GenericDataType();
    public static final PersistentDataType<String, Map> MAP_DATA_TYPE = new MapDataType();
    private static final Map<Class<?>, PersistentDataType<?, ?>> PRIMITIVE_MAP = new ConcurrentHashMap<>();

    public static <C> PersistentDataType<?, C> getPersistentDataType(Class<C> type) {
        if (type.isInterface()) throw new IllegalStateException("type cannot be interface");
        if (PRIMITIVE_MAP.containsKey(type)) return (PersistentDataType<?, C>) PRIMITIVE_MAP.get(type);
        return (PersistentDataType<?, C>) GENERIC_DATA_TYPE;
    }

    public static PersistentDataType<byte[], Object> getPersistentDataType() {
        return GENERIC_DATA_TYPE;
    }

    static {
        PRIMITIVE_MAP.put(Byte.class, PersistentDataType.BYTE);
        PRIMITIVE_MAP.put(byte.class, PersistentDataType.BYTE);
        PRIMITIVE_MAP.put(Short.class, PersistentDataType.SHORT);
        PRIMITIVE_MAP.put(short.class, PersistentDataType.SHORT);
        PRIMITIVE_MAP.put(Integer.class, PersistentDataType.INTEGER);
        PRIMITIVE_MAP.put(int.class, PersistentDataType.INTEGER);
        PRIMITIVE_MAP.put(Long.class, PersistentDataType.LONG);
        PRIMITIVE_MAP.put(long.class, PersistentDataType.LONG);
        PRIMITIVE_MAP.put(Float.class, PersistentDataType.FLOAT);
        PRIMITIVE_MAP.put(float.class, PersistentDataType.FLOAT);
        PRIMITIVE_MAP.put(Double.class, PersistentDataType.DOUBLE);
        PRIMITIVE_MAP.put(double.class, PersistentDataType.DOUBLE);
        PRIMITIVE_MAP.put(String.class, PersistentDataType.STRING);
        PRIMITIVE_MAP.put(byte[].class, PersistentDataType.BYTE_ARRAY);
        PRIMITIVE_MAP.put(int[].class, PersistentDataType.INTEGER_ARRAY);
        PRIMITIVE_MAP.put(long[].class, PersistentDataType.LONG_ARRAY);
    }

    public static class MapDataType implements PersistentDataType<String, Map> {

        @Override
        public @NotNull Class<String> getPrimitiveType() {
            return String.class;
        }

        @Override
        public @NotNull Class<Map> getComplexType() {
            return Map.class;
        }

        @Override
        public @NotNull String toPrimitive(@NotNull Map map, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
            return GSON.toJson(map);
        }

        @Override
        public @NotNull Map<?, ?> fromPrimitive(@NotNull String s, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
            return GSON.fromJson(s, Map.class);
        }
    }

    public static class GenericDataType implements PersistentDataType<byte[], Object> {

        @Override
        public @NotNull Class<byte[]> getPrimitiveType() {
            return byte[].class;
        }

        @Override
        public @NotNull Class<Object> getComplexType() {
            return Object.class;
        }

        @Override
        public byte @NotNull [] toPrimitive(@NotNull Object o, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
            try {
                return MAPPER_GETTER.get().writeValueAsBytes(o);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public @NotNull Object fromPrimitive(byte @NotNull [] bb, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
            try {
                return MAPPER_GETTER.get().readValue(bb, Object.class);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }


    public static <T> Map<String, Object> reflectToMap(T model) {

        if (model == null) return Map.of();

        var map = new LinkedHashMap<String, Object>();

        var reflecter = ELDependenci.getApi().exposeService(ReflectionService.class);
        List<Field> fields = reflecter.getDeclaredFieldsUpTo(model.getClass(), null);

        for (Field field : fields) {

            int mod = field.getModifiers();
            if (Modifier.isTransient(mod) || Modifier.isStatic(mod) || field.isAnnotationPresent(JsonIgnore.class)) {
                continue;
            }

            try {
                if (!field.trySetAccessible()) {
                    continue;
                }
                field.setAccessible(true);
                var value = field.get(model);
                map.put(field.getName(), value.toString());
            } catch (Exception e) {
                LOGGER.warn("Cannot get field {} from {}: {} ({})", field.getName(), model.getClass(), e.getMessage(), e.getClass().getSimpleName());
            }
        }

        return map;
    }

    public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) {
        T obj;
        try {
            obj = beanClass.getConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("The no arg constructor is private of type: " + beanClass);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Cannot find no arg constructor on type: " + beanClass);
        }
        var reflecter = ELDependenci.getApi().exposeService(ReflectionService.class);
        List<Field> fields = reflecter.getDeclaredFieldsUpTo(beanClass, null);
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isFinal(mod) || Modifier.isStatic(mod) || field.isAnnotationPresent(JsonIgnore.class)) {
                continue;
            }
            field.setAccessible(true);
            Object value = map.get(field.getName());
            if (value instanceof Map<?, ?> && field.getType() != Map.class) {
                Map<String, Object> m = (Map<String, Object>) value;
                value = mapToObject(m, field.getType());
            }
            if (value == null && field.isAnnotationPresent(NotNull.class)) {
                throw new IllegalStateException("property assigned @Nonnull but setting null value.");
            }
            try {
                field.set(obj, value);
            } catch (IllegalAccessException e) {
                LOGGER.warn("Error while setting field " + field.getName() + " to " + value + ", type: " + field.getType(), e);
            }
        }
        return obj;
    }

    public static Map<String, Object> toNestedMap(Map<String, Object> map) {
        HashMap<String, Object> result = new HashMap<>();
        var nested = new HashMap<String, Map<String, Object>>();
        map.forEach((k, v) -> {
            final int indexOfDot = k.indexOf('.');
            if (indexOfDot == -1) {
                result.put(k, v);
            } else {
                String[] paths = k.split("\\.");
                var remain = String.join("", Arrays.copyOfRange(paths, 1, paths.length));
                nested.putIfAbsent(paths[0], new HashMap<>());
                nested.get(paths[0]).put(remain, v);
            }
        });
        nested.forEach((key, mapp) -> result.put(key, toNestedMap(mapp)));
        return result;
    }
}
