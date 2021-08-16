package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.Component;
import com.ericlam.mc.eldgui.component.ComponentFactory;

import java.util.List;
import java.util.function.Function;

public interface SelectionFactory extends ComponentFactory<SelectionFactory> {

    SelectionFactory label(String title);

    <T> SelectionSettings<T> selectable(List<T> selections);

    interface SelectionSettings<T> {

        SelectionSettings<T> toDisplay(Function<T, String> toText);

        SelectionSettings<T> bindInput(String field, T value);

        Component create();

    }


}
