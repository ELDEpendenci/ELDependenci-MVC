package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;

import java.time.LocalDate;
import java.util.Date;

public interface DateSelectorFactory extends ComponentFactory<DateSelectorFactory> {

    DateSelectorFactory disabled();

    DateSelectorFactory label(String title);

    DateSelectorFactory bindInput(String field, LocalDate initValue);

    DateSelectorFactory inputMessage(String message);

    DateSelectorFactory invalidMessage(String message);

    DateSelectorFactory waitForInput(long maxWait);

}
