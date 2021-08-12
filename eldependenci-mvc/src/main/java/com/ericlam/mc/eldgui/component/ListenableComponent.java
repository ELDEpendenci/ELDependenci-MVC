package com.ericlam.mc.eldgui.component;

import org.bukkit.event.player.PlayerEvent;

// waiting for a specific event to accept and edit item attributes when clicked
public interface ListenableComponent<E extends PlayerEvent> extends Component {

    void callBack(E event);

    Class<E> getEventClass();


}
