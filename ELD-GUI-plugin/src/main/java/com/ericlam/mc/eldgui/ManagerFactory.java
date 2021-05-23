package com.ericlam.mc.eldgui;

import com.ericlam.mc.eldgui.event.MethodParseManager;
import com.ericlam.mc.eldgui.event.ReturnTypeManager;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

public final class ManagerFactory {


    public MethodParseManager buildParseManager(Consumer<MethodParseManager> parseConsumer) {
        MethodParseManager methodParseManager = new MethodParseManager();
        parseConsumer.accept(methodParseManager);
        return methodParseManager;
    }


    public ReturnTypeManager buildReturnTypeManager(Consumer<ReturnTypeManager> returnTypeManagerConsumer) {
        ReturnTypeManager returnTypeManager = new ReturnTypeManager();
        returnTypeManagerConsumer.accept(returnTypeManager);
        return returnTypeManager;
    }


    public <E> E buildManger(Class<E> managerCls, Consumer<E> consumer) {
        try {
            var constructor = managerCls.getConstructor();
            var manager = constructor.newInstance();
            consumer.accept(manager);
            return manager;
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("class " + managerCls + " must have empty arg constructor", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

}
