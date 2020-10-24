package de.espen.advancedtablist.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TabListUpdatePlayerEvent extends Event implements Cancellable {

    public static HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private String header;
    private String footer;
    private Player player;

    public TabListUpdatePlayerEvent(String header, String footer, Player player) {
        this.header = header;
        this.footer = footer;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public String getHeader() {
        return header;
    }

    public String getFooter() {
        return footer;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
