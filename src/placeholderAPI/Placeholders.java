package placeholderAPI;

import org.bukkit.entity.Player;

public abstract interface Placeholders {
    public abstract String setPlaceholders(Player paramPlayer, String paramString);

    public abstract String pluginHooked();
}