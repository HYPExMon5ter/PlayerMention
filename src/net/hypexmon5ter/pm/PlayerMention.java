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

    //public String messagePrefix = getConfig().getString("messagePrefix");
    public boolean useOldWay;
    public boolean needsPrefix;
    public boolean essentialsHook;
    public boolean factionChatHook;
    public boolean mcmmoHook;
    public boolean premiumVanishHook;

    public String consolePrefix = "[PlayerMention] ";
    public String regPrefix = getConfig().getString("Regular.prefix");

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    public boolean isPAPIEnabled;
    public boolean isMVdWEnabled;
    public boolean isEssentialsEnabled;
    public boolean isFactionChatEnabled;
    public boolean isMcmmoEnabled;
    public boolean isPremiumVanishEnabled;


    public void onEnable() {
        isPAPIEnabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        isMVdWEnabled = Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI");
        isEssentialsEnabled = Bukkit.getPluginManager().isPluginEnabled("Essentials");
        isFactionChatEnabled = Bukkit.getPluginManager().isPluginEnabled("FactionChat");
        isMcmmoEnabled = Bukkit.getPluginManager().isPluginEnabled("mcMMO");
        isPremiumVanishEnabled = Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish");

        getConfig().options().copyDefaults(true);
        saveConfig();

        checkHooks();

        PluginDescriptionFile pdfFile = getDescription();

        console.sendMessage(consolePrefix + "§ahas successfully loaded, enjoy!");
        console.sendMessage("§8-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        console.sendMessage("Info:");
        console.sendMessage("Running version: " + pdfFile.getVersion());
        if (isPAPIEnabled)
            console.sendMessage("Hooked into PlaceholderAPI");
        if (isMVdWEnabled)
            console.sendMessage("Hooked into MVdW's Placeholder API");
        if (isEssentialsEnabled)
            console.sendMessage("Hooked into Essentials");
        if (isFactionChatEnabled)
            console.sendMessage("Hooked into FactionChat");
        if (isMcmmoEnabled)
            console.sendMessage("Hooked into mcMMO");
        if (isPremiumVanishEnabled)
            console.sendMessage("Hooked into Premium/Super Vanish");
        console.sendMessage("§8-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

        getServer().getPluginManager().registerEvents(new MentionListener(this), this);
    }

    public void onDisable() {

    }

    public String convertPlaceholders(Player p, String msg) {
        if (isPAPIEnabled)
            return PlaceholderAPI.setPlaceholders(p.getPlayer(), msg);
        else if (isMVdWEnabled)
            return be.maximvdw.placeholderapi.PlaceholderAPI.replacePlaceholders(p.getPlayer(), msg);
        else
            return msg;
    }

    public void checkHooks() {
        useOldWay = getConfig().getBoolean("useOldWay");
        needsPrefix = getConfig().getBoolean("needsPrefix");
        essentialsHook = getConfig().getBoolean("hooks.Essentials");
        factionChatHook = getConfig().getBoolean("hooks.FactionChat");
        mcmmoHook = getConfig().getBoolean("hooks.mcMMO");
        premiumVanishHook = getConfig().getBoolean("hooks.PremiumVanish");
    }
}