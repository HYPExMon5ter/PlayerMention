package net.hypexmon5ter.pm;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerMention extends JavaPlugin implements Listener {

    public Set<UUID> exluded = new HashSet<>();
    public Set<Player> cooldown = new HashSet<>();

    public boolean useOldWay = getConfig().getBoolean("useOldWay");
    public boolean needsPrefix = getConfig().getBoolean("needsPrefix");
    public String prefix = getConfig().getString("Regular.prefix");

    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        getServer().getPluginManager().registerEvents(new MentionListener(this), this);
    }

    public void onDisable() {
        cooldown.clear();
    }
}
