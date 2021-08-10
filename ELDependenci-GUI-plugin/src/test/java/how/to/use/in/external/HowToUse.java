package how.to.use.in.external;

import com.ericlam.mc.eldgui.UIContext;
import com.ericlam.mc.eldgui.view.BukkitRedirectView;

public class HowToUse {


    public static void main(String[] args) {
        var r = new BukkitRedirectView(TestRedirectView.class, "abc");
        System.out.println(r.getView());
    }

    public static class TestRedirectView implements BukkitRedirectView.RedirectView {
        @Override
        public void renderView(String model, UIContext context) {

        }
    }
}
