package com.ericlam.mc.eldgui.component.modifier;

/**
 * 可執行動畫
 */
public interface Animatable {

    /**
     * 執行動畫
     */
    void startAnimation();

    /**
     *
     * @return 是否在動畫狀態
     */
    boolean isAnimating();

    /**
     * 停止動畫
     */
    void stopAnimation();

}
