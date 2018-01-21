package net.hypexmon5ter.pm.skript;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.hypexmon5ter.pm.events.OnMentionEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ExprPlayerMentioned extends SimpleExpression<Player> {

    @Override
    protected Player[] get(Event event) {
        if (event instanceof OnMentionEvent)
            return new Player[]{((OnMentionEvent)event).getMentioner()};
        return new Player[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Player> getReturnType() {
        return Player.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return "mentioned player";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if (ScriptLoader.isCurrentEvent(OnMentionEvent.class))
            return true;
        Skript.error("You can only use the mentioned player in 'on mention' event!");
        return false;
    }
}