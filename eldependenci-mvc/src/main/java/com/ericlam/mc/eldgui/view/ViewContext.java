package com.ericlam.mc.eldgui.view;

import com.ericlam.mc.eldgui.component.Component;
import com.ericlam.mc.eldgui.component.ComponentFactory;

public interface ViewContext {

    PatternComponentBuilder pattern(char pattern);

    <T extends ComponentFactory<T>> T factory(Class<T> factoryCls);

    interface PatternComponentBuilder {

        PatternComponentBuilder fill(Component component);

        PatternComponentBuilder components(Component... components);

        PatternComponentBuilder component(int pos, Component component);

        ViewContext and();

    }

}
