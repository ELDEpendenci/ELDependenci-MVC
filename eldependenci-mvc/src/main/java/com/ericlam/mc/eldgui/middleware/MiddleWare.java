package com.ericlam.mc.eldgui.middleware;

import java.lang.annotation.Annotation;

/**
 * 中間件攔截器
 * @param <A> 攔截器標註
 */
public interface MiddleWare<A extends Annotation> {

    /**
     * 攔截方法
     * @param context 中間件 context
     * @param annotation 攔截器標註
     * @throws Exception 攔截器拋出的例外
     */
    void intercept(InterceptContext context, A annotation) throws Exception;


}
