package skript;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import events.OnMentionEvent;
import org.bukkit.entity.Player;

public class EventVals {

    public EventVals() {
        EventValues.registerEventValue(OnMentionEvent.class, Player.class, new Getter<Player, OnMentionEvent>() {
            @Override
            public Player get(OnMentionEvent event) {
                return event.getPlayerMentioned();
            }
        }, 0);
    }
}
