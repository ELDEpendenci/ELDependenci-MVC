package com.ericlam.mc.eldgui.demo.async;

import com.ericlam.mc.eldgui.component.factory.ButtonFactory;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.View;
import com.ericlam.mc.eldgui.view.ViewDescriptor;

@ViewDescriptor(
        name = "async test",
        rows = 1,
        patterns = "ZZZZAZZZZ",
        cancelMove = 'A'
)
public final class AsyncView implements View<Void> {

    @Override
    public void renderView(Void model, UIContext context) {
        ButtonFactory button = context.factory(ButtonFactory.class);
        context.pattern('A').components(button.title("This is async view!").create());
    }
}
