package com.ericlam.mc.eldgui.demo.user;

import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.controller.ItemAttribute;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.event.ClickMapping;
import com.ericlam.mc.eldgui.view.BukkitRedirectView;
import com.ericlam.mc.eldgui.view.BukkitView;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@UIController("user")
public class UserController {

    @Inject
    private UserService userService;

    // index
    public BukkitView<?, ?> index(){
        List<String> users = userService.findAllUsernames();
        return new BukkitView<>(UserListView.class, users);
    }


    @ClickMapping(view = UserListView.class, pattern = 'L')
    public BukkitView<?, ?> onClickUser(@ItemAttribute("username") String username) throws UserNotFoundException {
        Optional<User> user = userService.findById(username);
        if (user.isPresent()){
            return new BukkitView<>(UserView.class, user.get());
        }else{
            // try using throw exception
            //return new BukkitView<>(UserNotFoundView.class, username);
            throw new UserNotFoundException(username);
        }
    }

    @ClickMapping(view = UserListView.class, pattern = 'M')
    public BukkitView<?, ?> onResetClick(){
        userService.reset();
        return index();
    }

    @ClickMapping(view = UserNotFoundView.class, pattern = 'A')
    public BukkitView<?, ?> backFromNotFound(){
        return index();
    }

    @ClickMapping(view = UserView.class, pattern = 'B')
    public BukkitView<?, ?> backFromUser(){
        return index();
    }

    @ClickMapping(view = UserView.class, pattern = 'C')
    public BukkitView<?, ?> deleteUser(@ItemAttribute("to-delete") String username, Player player){
        userService.removeUser(username);
        player.sendMessage("Delete Success");
        return index();
    }

    public static class UserNotFoundException extends Exception {

        public UserNotFoundException(String username) {
            super("username not found: "+username);
            this.username = username;
        }

        private final String username;

        public String getUsername() {
            return username;
        }

    }
}
