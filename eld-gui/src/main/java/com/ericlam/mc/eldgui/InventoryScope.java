package com.ericlam.mc.eldgui;

public interface InventoryScope{


    void setAttributeIfAbsent(String key, Object value);

    void setAttribute(String key, Object value);

    Object getAttribute(String key);

    <T> T getAttribute(String key, T defaultValue);

    <T> T getAttribute(String key, Class<T> type);

    InventoryScope getSessionScope();

}
