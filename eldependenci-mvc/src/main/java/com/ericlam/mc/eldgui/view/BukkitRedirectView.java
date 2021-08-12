package com.ericlam.mc.eldgui.view;

public class BukkitRedirectView extends BukkitView<BukkitRedirectView.RedirectView, String> {

    private final boolean customTransition;

    public BukkitRedirectView(String redirectTo) {
        super(RedirectView.class, redirectTo);
        this.customTransition = false;
    }

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

    public interface RedirectView extends View<String> {
    }

}
