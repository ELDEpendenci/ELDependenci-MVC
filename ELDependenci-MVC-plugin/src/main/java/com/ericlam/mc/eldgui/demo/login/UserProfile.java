package com.ericlam.mc.eldgui.demo.login;

public class UserProfile extends LoginSession {

    public boolean isAdmin;

    public UserProfile(String username, String playerName, boolean isAdmin) {
        super(username, playerName);
        this.isAdmin = isAdmin;
    }


}
