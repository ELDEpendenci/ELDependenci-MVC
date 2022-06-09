package com.ericlam.mc.eldgui;

import com.ericlam.mc.eldgui.component.ComponentFactory;
import com.ericlam.mc.eldgui.exception.ExceptionViewHandler;
import com.ericlam.mc.eldgui.middleware.MiddleWare;
import com.ericlam.mc.eldgui.view.LoadingView;
import org.bukkit.event.inventory.InventoryInteractEvent;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * ELDependenci-MVC 安裝器
 */
public interface MVCInstallation {

    /**
     * 安裝界面互動事件自定義過濾
     *
     * @param qualifier 指定的標注
     * @param predicate 指定自定義過濾
     * @param <A>       標注類
     */
    <A extends Annotation> void registerQualifier(Class<A> qualifier, QualifierFilter<A> predicate);

    <A extends Annotation, M extends MiddleWare<A>> void registerMiddleWare(Class<A> qualifier, Class<M> middleWareClass);

    /**
     * 安裝 controllers (控制器)
     *
     * @param controllers controllers
     */
    void registerControllers(Class<?>... controllers);

    /**
     * 新增自定義異常處理器
     *
     * @param exceptionHandlers 異常處理器
     */
    void addExceptionViewHandlers(List<Class<? extends ExceptionViewHandler>> exceptionHandlers);

    /**
     * 設置全局異常處理器
     * @param exceptionHandler 異常處理器
     */
    void setGlobalExceptionHandler(Class<? extends ExceptionViewHandler> exceptionHandler);

    /**
     * 設置全局異步加載界面
     * @param loadingView 異步加載界面
     */
    void setGlobalLoadingView(Class<? extends LoadingView> loadingView);

    /**
     * 新增自定義組件工廠
     * @param factory 組件類，必須爲 interface
     * @param implement 組件實作類
     * @param <T> 組件工廠類
     * @param <E> 組件工廠實作類
     */
    <T extends ComponentFactory<T>, E extends T> void addComponentFactory(Class<T> factory, Class<E> implement);

    @FunctionalInterface
    interface QualifierFilter<A extends Annotation> {
        boolean checkIsPass(InventoryInteractEvent interactEvent, char pattern, A annotation);
    }

}
