package com.ericlam.mc.eldgui.view;

import com.ericlam.mc.eld.services.ItemStackService;

public interface View<T> {

    void renderView(T model, UIContext context);

    // test
    default void renderView(T model, ViewContext context){}

    // test only
    default void setItemStackService(ItemStackService itemStackService){}

}
