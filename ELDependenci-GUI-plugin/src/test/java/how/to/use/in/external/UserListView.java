package how.to.use.in.external;

import com.ericlam.mc.eldgui.UIContext;
import com.ericlam.mc.eldgui.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserListView implements View<List<User>> {

    @Override
    public void renderView(List<User> model, UIContext context) {
        List<User> users = Optional.ofNullable(model).orElseGet(ArrayList::new);

        // render the ui....
        System.out.println("listing usernames: ");
        System.out.println(users.stream().map(u -> u.username).collect(Collectors.toList()));
        System.out.println("input a username to show info.");
    }
}
