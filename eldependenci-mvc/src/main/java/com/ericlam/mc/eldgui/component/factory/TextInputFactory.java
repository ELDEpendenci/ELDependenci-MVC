package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;

public interface TextInputFactory extends ComponentFactory<TextInputFactory> {

    TextInputFactory label(String label);

    TextInputFactory bindInput(String field, String initValue);

    TextInputFactory disabled();

}
