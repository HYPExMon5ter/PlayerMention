package placeholderStuff;

import org.bukkit.entity.Player;

public class EmptyPlaceholderHook extends PlaceholderSupport {

    @Override
    public void setPlaceHolder(Player p, String msg) {
        //Leave it blank, since it doesn't have the placeholder, it will take no actions
    }
}
