package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;

public interface NumInputFactory extends ComponentFactory<NumInputFactory> {

    NumInputFactory min(int min);

    NumInputFactory max(int max);

    NumInputFactory label(String label);

    NumInputFactory bindInput(String field, int initValue);

}
