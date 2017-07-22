package net.hypexmon5ter.pm;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import placeholderStuff.EmptyPlaceholderHook;
import placeholderStuff.PlaceholderHook;
import placeholderStuff.PlaceholderSupport;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerMention extends JavaPlugin implements Listener {

    public Set<UUID> excluded = new HashSet<>();
    public ArrayList<Player> cooldown = new ArrayList<>();

    public boolean useOldWay = getConfig().getBoolean("useOldWay");
    public boolean needsPrefix = getConfig().getBoolean("needsPrefix");

    public String consolePrefix = "[PlayerMention] ";
    public String regPrefix = getConfig().getString("Regular.prefix");

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        PluginDescriptionFile pdfFile = getDescription();

        console.sendMessage("§8-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        console.sendMessage(consolePrefix + "§ahas successfully loaded, enjoy!");
        console.sendMessage("Info:");
        console.sendMessage("Running version: " + pdfFile.getVersion());
        console.sendMessage("§8-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

        getServer().getPluginManager().registerEvents(new MentionListener(this), this);
    }

    public void onDisable() {

    }

    public PlaceholderSupport phs;
    public PlaceholderSupport getPlaceHolder() {
        if (phs == null) {
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) // check the plugin here
                phs = new PlaceholderHook();
            else
                phs = new EmptyPlaceholderHook();
        }
        return phs;
    }
}
