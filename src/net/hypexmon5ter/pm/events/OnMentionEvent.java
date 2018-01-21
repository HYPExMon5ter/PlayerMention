package net.hypexmon5ter.pm.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnMentionEvent extends Event implements Cancellable {

    Player mentioner;
    Player mentioned;

    boolean isCancelled;

    public OnMentionEvent(Player mentioner, Player mentioned) {
        this.mentioner = mentioner;
        this.mentioned = mentioned;
    }

    public Player getMentioner() {
        return mentioner;
    }

    public Player getPlayerMentioned() {
        return mentioned;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }
}