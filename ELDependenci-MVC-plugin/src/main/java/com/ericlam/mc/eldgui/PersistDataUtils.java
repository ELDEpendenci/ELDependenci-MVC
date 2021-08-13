package com.ericlam.mc.eldgui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.inject.TypeLiteral;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class PersistDataUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistDataUtils.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Gson GSON = new Gson();

    public static final PersistentDataType<String, Object> GENERIC_DATA_TYPE = new GenericDataType();
    public static final PersistentDataType<String, Map> MAP_DATA_TYPE = new MapDataType();
    private static final Map<Class<?>, PersistentDataType<?, ?>> PRIMITIVE_MAP = new ConcurrentHashMap<>();

    public static <C> PersistentDataType<?, C> getPersistentDataType(Class<C> type){
        if (type.isInterface()) throw new IllegalStateException("type cannot be interface");
        if (PRIMITIVE_MAP.containsKey(type)) return (PersistentDataType<?, C>) PRIMITIVE_MAP.get(type);
        return (PersistentDataType<?, C>) GENERIC_DATA_TYPE;
    }
    public static PersistentDataType<String, Object> getPersistentDataType(){
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

    public static class GenericDataType implements PersistentDataType<String, Object> {

        @Override
        public @NotNull Class<String> getPrimitiveType() {
            return String.class;
        }

        @Override
        public @NotNull Class<Object> getComplexType() {
            return Object.class;
        }

        @Override
        public @NotNull String toPrimitive(@NotNull Object o, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
            return GSON.toJson(o);
        }

        @Override
        public @NotNull Object fromPrimitive(@NotNull String s, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
            return GSON.fromJson(s, Object.class);
        }
    }


    public static Map<String, Object> reflectToMap(Object model) {
        if (model == null || model.getClass().isPrimitive()) return Map.of();
        return (Map<String, Object>) OBJECT_MAPPER.convertValue(model, Map.class);
    }

    public static <T> T mapToObject(Map<String, Object> map, Class<T> type){
        return OBJECT_MAPPER.convertValue(map, type);
    }
}
