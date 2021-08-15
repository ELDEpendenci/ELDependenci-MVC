package com.ericlam.mc.eldgui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.inject.TypeLiteral;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class PersistDataUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistDataUtils.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final Gson GSON = new Gson();

    public static final PersistentDataType<byte[], Object> GENERIC_DATA_TYPE = new GenericDataType();
    public static final PersistentDataType<String, Map> MAP_DATA_TYPE = new MapDataType();
    private static final Map<Class<?>, PersistentDataType<?, ?>> PRIMITIVE_MAP = new ConcurrentHashMap<>();

    public static <C> PersistentDataType<?, C> getPersistentDataType(Class<C> type){
        if (type.isInterface()) throw new IllegalStateException("type cannot be interface");
        if (PRIMITIVE_MAP.containsKey(type)) return (PersistentDataType<?, C>) PRIMITIVE_MAP.get(type);
        return (PersistentDataType<?, C>) GENERIC_DATA_TYPE;
    }
    public static PersistentDataType<byte[], Object> getPersistentDataType(){
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
                return OBJECT_MAPPER.writeValueAsBytes(o);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public @NotNull Object fromPrimitive(byte @NotNull [] bb, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
            try {
                return OBJECT_MAPPER.readValue(bb, Object.class);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }


    public static Map<String, Object> reflectToMap(Object model) {
        try {
            return OBJECT_MAPPER.convertValue(model, new TypeReference<>() {});
        }catch (Exception e){
            return Map.of();
        }
    }

    public static <T> T mapToObject(Map<String, Object> map, Class<T> type){
        return OBJECT_MAPPER.convertValue(map, type);
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