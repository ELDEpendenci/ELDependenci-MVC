package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.services.ConfigPoolService;
import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.controller.ControllerForView;
import com.ericlam.mc.eldgui.model.Model;
import com.ericlam.mc.eldgui.view.UseTemplate;
import com.ericlam.mc.eldgui.view.View;
import com.ericlam.mc.eldgui.view.ViewDescriptor;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ELDGInventoryService implements InventoryService{

    @Inject
    private Map<String, UIController> uiControllerMap;

    @Inject
    private MethodParseFactory methodParseFactory;

    @Inject
    private ItemStackService itemStackService;

    @Inject
    private ConfigPoolService configPoolService;

    @Inject
    private ELDGLanguage language;

    private final Map<String, UIDispatcher> uiDispatcherMap = new ConcurrentHashMap<>();

    @Override
    public UIDispatcher getUIDispatcher(String name) throws UINotFoundException {
        if (uiDispatcherMap.containsKey(name)) return uiDispatcherMap.get(name);
        UIController controller = Optional.ofNullable(uiControllerMap.get(name)).orElseThrow(() -> new UINotFoundException(MessageFormat.format(language.getLang().get("not-found"), name)));
        Class<? extends UIController> controllerCls = controller.getClass();
        if (!controllerCls.isAnnotationPresent(ControllerForView.class)) throw new IllegalStateException("@ControllerForView annotation is lacked.");
        ControllerForView controllerForView = controllerCls.getAnnotation(ControllerForView.class);
        Class<? extends View<? extends Model>> viewCls = controllerForView.value();
        InventoryTemplate template;
        if (viewCls.isAnnotationPresent(UseTemplate.class)){
            UseTemplate useTemplate = viewCls.getAnnotation(UseTemplate.class);
            var pool = configPoolService.getPool(useTemplate.groupResource());
            if (pool == null) throw new IllegalStateException("config pool is not loaded: "+useTemplate.groupResource());
            template = Optional.ofNullable(pool.get(useTemplate.template())).orElseThrow(() -> new IllegalStateException("Cannot find template: "+useTemplate.template()));
        }else if (viewCls.isAnnotationPresent(ViewDescriptor.class)){
            ViewDescriptor viewDescriptor = viewCls.getAnnotation(ViewDescriptor.class);
            template = new CodeInventoryTemplate(viewDescriptor);
        }else{
            throw new IllegalStateException("view is lack of @UseTemplate either @ViewDescriptor annotation.");
        }
        try {
            Constructor<? extends View<? extends Model>> constructor = viewCls.getConstructor(ItemStackService.class);
            View<? extends Model> view = constructor.newInstance(itemStackService);
            UIDispatcher dispatcher = new ELDGDispatcher<>(view, controller, methodParseFactory, itemStackService, template, (s, player, ui) -> this.getUIDispatcher(ui).openFor(player));
            this.uiDispatcherMap.put(name, dispatcher);
            return dispatcher;
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("view should follow it's parent constructor.");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
