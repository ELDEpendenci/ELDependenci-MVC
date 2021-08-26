package com.ericlam.mc.eldgui.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 獲取指定 pattern 内的所有物品，使用 {@code List<ItemStack>} 返回
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface FromPattern {

    /**
     *
     * @return 指定 pattern
     */
    char value();

}
