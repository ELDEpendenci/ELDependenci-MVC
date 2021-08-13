package com.ericlam.mc.eldgui.view;

import com.ericlam.mc.eldgui.component.Component;
import com.ericlam.mc.eldgui.component.ComponentFactory;

public interface UIContext {

    PatternComponentBuilder pattern(char pattern);

    <T extends ComponentFactory<T>> T factory(Class<T> factoryCls);

    interface PatternComponentBuilder {

        PatternComponentBuilder fill(Component component);

        PatternComponentBuilder components(Component... components);

        PatternComponentBuilder component(int pos, Component component);

        PatternComponentBuilder bindAll(String key, Object value);

        UIContext and();

    }

}
