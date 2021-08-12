package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;

public interface TextFieldFactory extends ComponentFactory<TextFieldFactory> {

    TextFieldFactory bindInput(String field, Object initValue);

}
