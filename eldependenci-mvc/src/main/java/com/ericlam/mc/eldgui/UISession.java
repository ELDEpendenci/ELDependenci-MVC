package com.ericlam.mc.eldgui;

import javax.annotation.Nullable;

public interface UISession {

    @Nullable
    <T> T getAttribute(String key);

    @Nullable
    <T> T pollAttribute(String key);

    void setAttribute(String key, Object item);



}
