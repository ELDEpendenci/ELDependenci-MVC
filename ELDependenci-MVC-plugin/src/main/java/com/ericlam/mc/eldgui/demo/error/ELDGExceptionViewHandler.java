package com.ericlam.mc.eldgui.demo.error;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.UIContext;
import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.demo.user.UserController;
import com.ericlam.mc.eldgui.exception.ExceptionViewHandler;
import com.ericlam.mc.eldgui.exception.HandleException;
import com.ericlam.mc.eldgui.view.BukkitRedirectView;
import com.ericlam.mc.eldgui.view.BukkitView;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ELDGExceptionViewHandler implements ExceptionViewHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ELDGExceptionViewHandler.class);

    @Override
    public BukkitView<?, ?> createErrorView(Exception exception, String fromController, UISession session, Player player) {
        LOGGER.warn("Resolved Error: "+exception.getMessage(), exception);
        return new BukkitView<>(StaticErrorView.class, exception);
    }

    @HandleException(RuntimeException.class)
    public BukkitView<?, ?> createRuntimeErrorView(RuntimeException ex, String from, UISession session, Player player){
        LOGGER.warn("Resolved Error: "+ex.getMessage(), ex);
        session.setAttribute("exception", ex);
        session.setAttribute("from", from);
        return new BukkitRedirectView(CustomRedirectView.class, "error");
    }

    @HandleException(UserController.UserNotFoundException.class)
    public BukkitView<?, ?> handleUserNotFoundException(UserController.UserNotFoundException ex, String from, UISession session, Player player){
        LOGGER.warn("Resolved Error: "+ex.getMessage());
        session.setAttribute("exception", ex);
        session.setAttribute("from", from);
        return new BukkitRedirectView(CustomRedirectView.class, "error");
    }


    @ViewDescriptor(
            name = "&6Loading...",
            rows = 1,
            patterns = { "ZZZZZZZZZ" },
            cancelMove = {'Z'}
    )
    public static class CustomRedirectView implements BukkitRedirectView.RedirectView{

        private ItemStackService itemStackService;

        @Override
        public void setItemStackService(ItemStackService itemStackService) {
            this.itemStackService = itemStackService;
        }

        @Override
        public void renderView(String model, UIContext context) {
            context.fillItem('Z', itemStackService.build(Material.GREEN_STAINED_GLASS_PANE).getItem());
        }
    }
}