package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;

import java.time.LocalTime;

public interface TimeSelectorFactory extends ComponentFactory<TimeSelectorFactory> {

    TimeSelectorFactory disabled();

    TimeSelectorFactory label(String title);

    TimeSelectorFactory bindInput(String field, LocalTime time);

    TimeSelectorFactory inputMessage(String message);

    TimeSelectorFactory invalidMessage(String message);

    TimeSelectorFactory waitForInput(long maxWait);

}
