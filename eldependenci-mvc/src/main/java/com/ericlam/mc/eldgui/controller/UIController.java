package com.ericlam.mc.eldgui.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 標記爲 UIController (控制器)。
 * <br>
 * 你需要定義一個名爲 index 的方法，參數自取所需，必須返回 {@link com.ericlam.mc.eldgui.view.BukkitView} 作爲控制器初始界面。
 * <br>
 * 然後新增自定義方法用於處理界面互動，方法必須有 {@link com.ericlam.mc.eldgui.event } 内的標注，參數各取所需，可返回 {@link com.ericlam.mc.eldgui.view.BukkitView}
 * 或者 void
 * <br>
 * 可取參數詳情請到教學文件詳閱。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UIController {

    /**
     *
     * @return controller id (控制器id)
     */
    String value();

}
