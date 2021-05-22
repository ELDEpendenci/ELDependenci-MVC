package com.ericlam.mc.eldgui;

import javax.annotation.Nullable;

public interface UISession {

    @Nullable
    <T> T getAttribute(String key);

    void setAttribute(String key, Object item);

    boolean removeAttribute(String key);

}
