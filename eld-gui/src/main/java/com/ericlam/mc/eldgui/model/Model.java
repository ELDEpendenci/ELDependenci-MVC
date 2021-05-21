package com.ericlam.mc.eldgui.model;

public abstract class Model {

    private boolean changed;

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
