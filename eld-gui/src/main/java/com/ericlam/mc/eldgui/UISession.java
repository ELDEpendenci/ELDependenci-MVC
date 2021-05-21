package com.ericlam.mc.eldgui;

public interface UISession {

    <T> T getAttribute(String key);

    void setAttribute(String key, Object item);

}
