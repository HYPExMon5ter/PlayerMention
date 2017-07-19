package net.hypexmon5ter.pm;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerMention extends JavaPlugin implements Listener {

    public Set<UUID> excluded = new HashSet<>();
    public ArrayList<Player> cooldown = new ArrayList<>();

    public String consolePrefix = "[PlayerMention] ";
    public String prefix = getConfig().getString("prefix");
    public boolean useOldWay = getConfig().getBoolean("useOldWay");
    public boolean needsPrefix = getConfig().getBoolean("needsPrefix");
    public String regPrefix = getConfig().getString("Regular.prefix");

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        console.sendMessage("§8-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        console.sendMessage(consolePrefix + "§ahas successfully loaded, enjoy!");
        console.sendMessage("§8-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

        getServer().getPluginManager().registerEvents(new MentionListener(this), this);
    }

    public void onDisable() {

    }
}
