package net.hypexmon5ter.pm;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerMention extends JavaPlugin implements Listener {

    public Set<UUID> excluded = new HashSet<>();
    public ArrayList<Player> cooldown = new ArrayList<>();

    public String consolePrefix = "[PlayerMention] ";
    //public String messagePrefix = getConfig().getString("messagePrefix");
    public boolean useOldWay = getConfig().getBoolean("useOldWay");
    public boolean needsPrefix = getConfig().getBoolean("needsPrefix");
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
        /*if (setupPlaceholders()) {
            console.sendMessage("Hooked into PlaceholderAPI.");
        }*/
        console.sendMessage("§8-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

        getServer().getPluginManager().registerEvents(new MentionListener(this), this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {

    }

    public String convertPlaceholders(Player p, String msg) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            console.sendMessage("Parsing placeholders..");
            return PlaceholderAPI.setPlaceholders(p.getPlayer(), msg);
        } else {
            console.sendMessage("Couldn't parse placeholders, returning msg.");
            return msg;
        }
    }

/*    private boolean setupPlaceholders() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            placeholders = new PlaceholderAPIPlaceholders();
            return true;
        }
        //this.placeholders = new DeluxeJoinPlaceholders();
        placeholders = null;
        return false;
    }

    public Placeholders getPlaceholders() {
        return placeholders;
    }

    public void sendMessage(Player p, String msg) {
        if (getPlaceholders() != null) {
            msg = getPlaceholders().setPlaceholders(p, msg);
            p.sendMessage(msg);
        } else {
            p.sendMessage(msg);
        }
    }*/
}
