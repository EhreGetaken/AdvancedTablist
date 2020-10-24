package de.espen.advancedtablist.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class TabListUpdateEvent extends Event implements Cancellable {

    public static HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    int schedulerId;

    public TabListUpdateEvent(int schedulerId) {
        this.schedulerId = schedulerId;
    }

    public int getSchedulerId() {
        return schedulerId;
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
