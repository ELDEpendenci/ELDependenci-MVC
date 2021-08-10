package com.ericlam.mc.eldgui.demo.user;

import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.view.BukkitView;

import javax.inject.Inject;
import java.util.List;

@UIController("user")
public class UserController {

    @Inject
    private UserService userService;


    public BukkitView<?, ?> index(){
        List<User> users = userService.findAll();
        return new BukkitView<>(UserListView.class, users);
    }

}
