package com.ericlam.mc.eldgui.view;

/**
 * 跳轉界面
 */
public class BukkitRedirectView extends BukkitView<BukkitRedirectView.RedirectView, String> {

    private final boolean customTransition;

    /**
     *
     * @param redirectTo 要跳轉的 controller id
     */
    public BukkitRedirectView(String redirectTo) {
        super(RedirectView.class, redirectTo);
        this.customTransition = false;
    }

    /**
     * 連帶轉換界面的跳轉界面
     * @param transitionView 轉換界面(跳轉過程很快，可能看不見)
     * @param redirectTo 要跳轉的 controller id
     */
    @SuppressWarnings("unchecked")
    public BukkitRedirectView(Class<? extends RedirectView> transitionView, String redirectTo) {
        super((Class<RedirectView>) transitionView, redirectTo);
        this.customTransition = true;
    }

    public String getRedirectTo(){
        return getModel();
    }

    public boolean isCustomTransition() {
        return customTransition;
    }

    /**
     * 跳轉界面接口。如果要跳轉界面，請實作它
     */
    public interface RedirectView extends View<String> {
    }

}
