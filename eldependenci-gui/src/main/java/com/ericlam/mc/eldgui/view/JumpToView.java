package com.ericlam.mc.eldgui.view;

@Deprecated
public final class JumpToView {

    private final String view;
    private final boolean keepPreviousUI;

    public JumpToView(String view, boolean keepPreviousUI) {
        this.view = view;
        this.keepPreviousUI = keepPreviousUI;
    }

    public JumpToView(String view) {
        this(view, false);
    }

    public String getView() {
        return view;
    }

    public boolean isKeepPreviousUI() {
        return keepPreviousUI;
    }
}
