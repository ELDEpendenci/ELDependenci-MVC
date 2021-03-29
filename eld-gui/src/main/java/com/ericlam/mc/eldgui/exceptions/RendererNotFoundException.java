package com.ericlam.mc.eldgui.exceptions;

public class RendererNotFoundException extends Exception {

    public final String renderer;

    public RendererNotFoundException(String renderer) {
        super("找不到 Renderer: "+renderer);
        this.renderer = renderer;
    }
}
