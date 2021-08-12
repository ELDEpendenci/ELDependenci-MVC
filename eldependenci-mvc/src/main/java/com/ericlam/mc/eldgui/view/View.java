package com.ericlam.mc.eldgui.view;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.UIContext;

public interface View<T> {

    void renderView(T model, UIContext context);


    // test only
    default void setItemStackService(ItemStackService itemStackService){}

}
