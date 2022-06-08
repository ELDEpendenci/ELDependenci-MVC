package com.ericlam.mc.eldgui;

import com.ericlam.mc.eldgui.demo.login.AuthService;
import com.ericlam.mc.eldgui.demo.user.UserService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class ELDGMVCModule extends AbstractModule {

    private final ELDGMVCInstallation eldgmvcInstallation;

    public ELDGMVCModule(ELDGMVCInstallation eldgmvcInstallation) {
        this.eldgmvcInstallation = eldgmvcInstallation;
    }

    @Override
    protected void configure() {
        bind(UserService.class).in(Scopes.SINGLETON); // test only
        bind(AuthService.class).in(Scopes.SINGLETON); // test only
        bind(ELDGMVCInstallation.class).toInstance(eldgmvcInstallation);
    }
}
