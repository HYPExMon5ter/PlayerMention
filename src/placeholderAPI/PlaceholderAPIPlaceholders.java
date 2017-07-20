package placeholderAPI;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlaceholderAPIPlaceholders implements Placeholders {
    public String setPlaceholders(Player p, String msg) {
        return PlaceholderAPI.setPlaceholders(p, msg);
    }

    public String pluginHooked() {
        return "PlaceholderAPI";
    }
}
