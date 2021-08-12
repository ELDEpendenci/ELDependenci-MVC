package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;

public interface NumInputFactory extends ComponentFactory<NumInputFactory> {

    NumInputFactory bindInput(String field, int initValue);

}
