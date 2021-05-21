package com.ericlam.mc.eldgui;

import com.ericlam.mc.eldgui.model.Model;

public interface MutableLiveData<M extends Model> extends LiveData<M> {

    void setValue(M data);

    void postValue(M data);

    M value();

}
