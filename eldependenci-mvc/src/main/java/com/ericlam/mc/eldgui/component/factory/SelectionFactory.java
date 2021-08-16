package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;
import org.bukkit.Material;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface SelectionFactory extends ComponentFactory<SelectionFactory> {

    SelectionFactory label(String title);

    SelectionFactory disabled();

    <T> SelectionSettings<T> selectable(List<T> selections);

    <T> SelectionSettings<T> selectable(Class<T> type, Consumer<SelectionBuilder<T>> selectionBuilder);

    interface SelectionSettings<T> {

        SelectionSettings<T> toDisplay(Function<T, String> toText);

        SelectionSettings<T> bindInput(String field, T value);

        SelectionFactory then();

    }

    interface SelectionBuilder<T> {

        Selection<T> insert(T element);

        interface Selection<T> {

            Selection<T> number(int amount);

            Selection<T> icon(Material icon);

            void submit();

        }

    }


}
